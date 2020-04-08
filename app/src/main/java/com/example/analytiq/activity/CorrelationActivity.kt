package com.example.analytiq.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.analytiq.R
import kotlinx.android.synthetic.main.npv_irr_rows.view.*
import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class CorrelationActivity : AppCompatActivity() {

    lateinit var calculate: Button
    lateinit var import: Button
    lateinit var addRow: Button
    lateinit var resText: TextView
    lateinit var table: TableLayout
    lateinit var resTable: TableLayout
    lateinit var resTableHead: TableRow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_correlation)

        findViewById<ScrollView>(R.id.correaltion_scroll).isHorizontalScrollBarEnabled = false
        findViewById<ScrollView>(R.id.correaltion_scroll).isVerticalScrollBarEnabled = false

        calculate = findViewById(R.id.calculate)
        import = findViewById(R.id.importBtn)
        addRow = findViewById(R.id.addRow)
        resText = findViewById(R.id.corr_res)
        table = findViewById(R.id.corr_table)
        resTable = findViewById(R.id.resTable)
        resTableHead = findViewById(R.id.corr_table2_heading)

        findViewById<TextView>(R.id.txtxx).setText("X-X̄")
        findViewById<TextView>(R.id.txtyy).setText("X-Ȳ")
        findViewById<TextView>(R.id.txtxx2).setText("∑ " + Html.fromHtml("X-X̄ <sup>2</sup>"))
        findViewById<TextView>(R.id.txtyy).setText("∑ " + Html.fromHtml("Y-Ȳ <sup>2</sup>"))
        findViewById<TextView>(R.id.txtxy).setText("∑ (X-X̄)(Y-Ȳ)")


        for (i in 0..3) {
            val row = layoutInflater.inflate(R.layout.correlation_rows, null) as TableRow
            table.addView(row)
        }

        addRow.setOnClickListener {
            val row = layoutInflater.inflate(R.layout.correlation_rows, null) as TableRow
            table.addView(row)
        }

        calculate.setOnClickListener {

            resTable.removeAllViews()
            var sumX = 0.0
            var sumY = 0.0
            var countX = 0
            var countY = 0
            var sum3 = 0.0
            var sum4 = 0.0
            var sum5 = 0.0
            var finalAns = 0.0

            val decimalformat = DecimalFormat("#.###")
            var flag = 0
            for (i in 0 until table.childCount) {
                val row = table.getChildAt(i) as TableRow
                val xValue = row.getChildAt(0) as EditText
                val yValue = row.getChildAt(1) as EditText
                if (!TextUtils.isEmpty(xValue.text)) {
                    sumX += xValue.text.toString().toDouble()
                    countX++
                } else if (!TextUtils.isEmpty(xValue.text) && TextUtils.isEmpty(yValue.text)) {
                    flag = 1
                }
                if (!TextUtils.isEmpty(yValue.text)) {
                    sumY += yValue.text.toString().toDouble()
                    countY++
                } else if (!TextUtils.isEmpty(yValue.text) && TextUtils.isEmpty(xValue.text)) {
                    flag = 1
                }
            }

            if (flag == 1) {
                Toast.makeText(this@CorrelationActivity, "Fill properly", Toast.LENGTH_SHORT)
                    .show()
            }

            val xMean = sumX / countX
            val yMean = sumY / countY

            println("Means are " + xMean + "   " + yMean)

            for (i in 0 until table.childCount) {
                val row = table.getChildAt(i) as TableRow
                val xValueEdit = row.getChildAt(0) as EditText
                val yValueEdit = row.getChildAt(1) as EditText
                if (!xValueEdit.text.isEmpty() && !yValueEdit.text.isEmpty()) {

                    val xValue = xValueEdit.text.toString().toDouble()
                    val yValue = yValueEdit.text.toString().toDouble()

                    val xDifference = row.getChildAt(2) as EditText
                    xDifference.setText("%.3f".format(xValue - xMean))

                    val yDifference = row.getChildAt(3) as EditText
                    yDifference.setText("%.3f".format(yValue - yMean))

                    val diffProduct = row.getChildAt(4) as EditText
                    diffProduct.setText(("%.3f".format((xValue - xMean) * (yValue - yMean))))
                }
            }

            for (j in 0 until table.childCount) {
                val row1 = table.getChildAt(j) as TableRow
                val xdiff = (row1.getChildAt(2) as EditText)
                val ydiff = (row1.getChildAt(3) as EditText)
                val prod = (row1.getChildAt(4) as EditText)


                if (!xdiff.text.isEmpty() && !ydiff.text.isEmpty() && !prod.text.isEmpty()) {
                    sum3 += (xdiff.text.toString().toDouble() * xdiff.text.toString().toDouble())
                    sum4 += (ydiff.text.toString().toDouble() * ydiff.text.toString().toDouble())
                    sum5 += prod.text.toString().toDouble()
                }
            }

            val view1 = layoutInflater.inflate(R.layout.corr_res_rows, null)
            resTable.addView(view1)

            val row = resTable.getChildAt(0) as TableRow
            val et1 = row.getChildAt(0) as EditText
            val et2 = row.getChildAt(1) as EditText
            val et3 = row.getChildAt(2) as EditText

            et1.setText("%.3f".format(sum3))
            et2.setText("%.3f".format(sum4))
            et3.setText("%.3f".format(sum5))

            val denom = Math.sqrt(sum3 * sum4)

            if (denom == 0.0) {
                resText.setText("Correlation= 1")
            } else {

                finalAns = sum5 / denom

                resText.setText("Correlation= " + ("%.3f".format(finalAns).toString()))
            }
            resText.visibility = View.VISIBLE
            resTableHead.visibility = View.VISIBLE
        }

        import.setOnClickListener {

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("*/*")
            startActivityForResult(intent, 15)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            15 -> {
                if (resultCode == Activity.RESULT_OK) {
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
                                            this@CorrelationActivity,
                                            "Number of columns is greater than 2",
                                            Toast.LENGTH_SHORT
                                        )
                                        break
                                    }
                                }
                                table.removeAllViews()

                                for (i in 0 until rows) {
                                    val view = layoutInflater.inflate(
                                        R.layout.correlation_rows,
                                        null
                                    ) as TableRow
                                    table.addView(view)

                                    val row=sheet.getRow(i)
                                    val count=row.physicalNumberOfCells

                                    for(j in 0 until count){
                                        val value=getCellAsString(row,j,formulaEvaluator)
                                        val edit=view.getChildAt(j) as EditText
                                        edit.setText(value)
                                    }

                                }
                            } catch (e: IOException) {
                                Toast.makeText(
                                    this@CorrelationActivity,
                                    "Error reading input stream",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } catch (e: FileNotFoundException) {
                                Toast.makeText(
                                    this@CorrelationActivity,
                                    "File Not Found",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        } else {
                            Toast.makeText(
                                this@CorrelationActivity,
                                "File type not supported",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@CorrelationActivity,
                            "Some error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun getCellAsString(row: Row, c: Int, formulaEvaluator: FormulaEvaluator): String {
        var value = ""
        try {
            val cell = row.getCell(c)
            val cellValue = formulaEvaluator.evaluate(cell)
            if (cellValue!=null){
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
            }else{
                return ""
            }
        } catch (e: NullPointerException) {
            Toast.makeText(
                this@CorrelationActivity,
                "getCellAsString: NullPointerException: " + e.message,
                Toast.LENGTH_SHORT
            ).show()
        }

        return value
    }
}
