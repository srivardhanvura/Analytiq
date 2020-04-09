package com.example.analytiq.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.example.analytiq.R
import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileNotFoundException
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class DiscountingFactor : AppCompatActivity() {

    lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discounting_factor)

        findViewById<ScrollView>(R.id.discounting_scroll).isHorizontalScrollBarEnabled = false
        findViewById<ScrollView>(R.id.discounting_scroll).isVerticalScrollBarEnabled = false

        tableLayout = findViewById(R.id.table)

        for (i in 0 until 4) {
            val tableRow =
                getLayoutInflater().inflate(R.layout.discounting_factor_rows, null) as TableRow
            tableLayout.addView(tableRow)
//            TextView sno = (TextView) tableRow.getChildAt(0);
//            sno.setText(String.valueOf(tableLayout.getChildCount()));

        }
    }

    fun calculate(view: View) {
        val decimalFormat = DecimalFormat("#.###")


        for (i in 0 until tableLayout.childCount) {
            val tableRow = tableLayout.getChildAt(i) as TableRow
            val year = tableRow.getChildAt(0) as EditText
            val rate = tableRow.getChildAt(1) as EditText
            val discountingFactor = tableRow.getChildAt(2) as EditText

            var r: Double
            var df: Double
            var t: Double

            if (!TextUtils.isEmpty(year.getText()) && !TextUtils.isEmpty(rate.getText())) {
                r = (rate.getText().toString()).toDouble()
                t = (year.getText().toString()).toDouble()

                df = 1 / Math.pow(1 + (r / 100), t)

                discountingFactor.setText((decimalFormat.format(df)).toString())

            } else {
                if (!TextUtils.isEmpty(year.getText()) || !TextUtils.isEmpty(rate.getText())) {
                    Toast.makeText(this, "Fill Properly", Toast.LENGTH_SHORT).show()
                    break
                }
            }
        }
    }

    fun addRow(view: View) {
        val tableRow =
            layoutInflater.inflate(R.layout.discounting_factor_rows, null) as TableRow
        tableLayout.addView(tableRow)
    }

    fun importbtn(view: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("*/*")
        startActivityForResult(intent, 25)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 25 && resultCode == Activity.RESULT_OK) {
            val data1 = data?.data
            val path = data1?.path

            if (path != null) {
                if (path.endsWith(".xlsx")) {

                    try {
                        val inputstream = contentResolver.openInputStream(data1)
                        val excelfile = XSSFWorkbook(inputstream)
                        val sheet = excelfile.getSheetAt(0)
                        val formulaEvaluator =
                            excelfile.creationHelper.createFormulaEvaluator()

                        val rows = sheet.physicalNumberOfRows
                        for (i in 0 until rows) {
                            val row = sheet.getRow(i)
                            val cellCount = row.physicalNumberOfCells
                            if (cellCount > 2) {
                                Toast.makeText(
                                    this,
                                    "Number of columns is greater than 2",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return
                            }
                        }
                        tableLayout.removeAllViews()

                        for (i in 0 until rows) {
                            val view = layoutInflater.inflate(
                                R.layout.discounting_factor_rows,
                                null
                            ) as TableRow
                            tableLayout.addView(view)

                            val row = sheet.getRow(i)
                            val count = row.physicalNumberOfCells

                            for (j in 0 until count) {
                                val value = getCellAsString(row, j, formulaEvaluator)
                                val edit = view.getChildAt(j) as EditText
                                edit.setText(value)
                            }

                        }
                    } catch (e: IOException) {
                        Toast.makeText(
                            this,
                            "Error reading input stream",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } catch (e: FileNotFoundException) {
                        Toast.makeText(
                            this,
                            "File Not Found",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "File type not supported",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Some error occurred",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun getCellAsString(row: Row, c: Int, formulaEvaluator: FormulaEvaluator): String {
        var value = ""
        try {
            val cell = row.getCell(c)
            val cellValue = formulaEvaluator.evaluate(cell)
            if (cellValue != null) {
                when (cellValue.cellType) {
                    Cell.CELL_TYPE_BOOLEAN -> value = "" + cellValue.booleanValue
                    Cell.CELL_TYPE_NUMERIC -> {
                        val numericValue = cellValue.numberValue
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            val date = cellValue.numberValue
                            val formatter = SimpleDateFormat("MM/dd/yy")
                            value = formatter.format(HSSFDateUtil.getJavaDate(date))
                        } else {
                            value = "" + numericValue
                        }
                    }
                    Cell.CELL_TYPE_STRING -> value = "" + cellValue.stringValue
                }
            } else {
                return ""
            }
        } catch (e: NullPointerException) {
            Toast.makeText(
                this,
                "getCellAsString: NullPointerException: " + e.message,
                Toast.LENGTH_SHORT
            ).show()
        }

        return value
    }
}
