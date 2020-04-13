package com.example.analytiq.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

class MeanVarianceSD : AppCompatActivity() {

    lateinit var meanET: EditText
    lateinit var varianceET: EditText
    lateinit var sumET: EditText
    lateinit var SDET: EditText
    lateinit var tableLayout: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mean_variance_sd)

        findViewById<ScrollView>(R.id.mean_variance_scroll).isHorizontalScrollBarEnabled = false
        findViewById<ScrollView>(R.id.mean_variance_scroll).isVerticalScrollBarEnabled = false

        tableLayout = findViewById(R.id.table);
        sumET = findViewById(R.id.sum);
        meanET = findViewById(R.id.m);
        varianceET = findViewById(R.id.v);
        SDET = findViewById(R.id.sd);

        for (i in 0 until 4) {
            val tableRow =
                layoutInflater.inflate(R.layout.mean_varience_deviation_rows, null) as TableRow
            tableLayout.addView(tableRow)
        }
        findViewById<Button>(R.id.import_btn).setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("*/*")
                startActivityForResult(intent, 30)
            } else {
                askForPermission()
            }
        }
    }

    fun calculate(view: View) {

        val decimalFormat = DecimalFormat("#.###");

        var meanSum = 0.0
        var d2Sum = 0.0
        var n = 0


        for (i in 0 until tableLayout.childCount) {
            val tableRow = tableLayout.getChildAt(i) as TableRow
            val xET = tableRow.getChildAt(0) as EditText

            if (!TextUtils.isEmpty(xET.getText())) {

                n += 1;

                val x = (xET.getText().toString().toDouble())
                meanSum += x;
            }
        }
        val m = meanSum / (n.toString().toDouble())
        meanET.setText((decimalFormat.format(m)).toString())

        for (i in 0 until tableLayout.childCount) {
            val tableRow = tableLayout.getChildAt(i) as TableRow
            val xET = tableRow.getChildAt(0) as EditText
            val xyET = tableRow.getChildAt(1) as EditText
            val xyzET = tableRow.getChildAt(2) as EditText

            if (!TextUtils.isEmpty(xET.getText())) {

                val x = (xET.getText().toString().toDouble())

                val xy = x - m;
                xyET.setText((decimalFormat.format(xy)).toString())

                val xyz = Math.pow(xy, 2.0)
                xyzET.setText((decimalFormat.format(xyz)).toString())

                d2Sum += xyz;
            }
        }

        sumET.setText((decimalFormat.format(d2Sum)).toString())
        val v = d2Sum / ((n - 1).toString().toDouble())
        varianceET.setText((decimalFormat.format(v)).toString())
        SDET.setText((decimalFormat.format(Math.pow(v, 0.5))).toString())
    }

    fun addRow(view: View) {
        val tableRow =
            layoutInflater.inflate(R.layout.mean_varience_deviation_rows, null) as TableRow
        tableLayout.addView(tableRow)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 30 && resultCode == Activity.RESULT_OK) {
            val data1 = data?.data
            val path = data1?.path
            if (path != null) {
                if (path.endsWith(".xlsx")) {
                    try {
                        val inputStream = contentResolver?.openInputStream(data1)
                        val excelFile = XSSFWorkbook(inputStream)
                        val sheet = excelFile.getSheetAt(0)
                        val rows = sheet.physicalNumberOfRows
                        val formulaevaluator = excelFile.creationHelper.createFormulaEvaluator()
                        for (i in 0 until rows) {
                            val row = sheet.getRow(i)
                            if (row.physicalNumberOfCells > 3) {
                                Toast.makeText(
                                    this,
                                    "Number of columns are more than 2",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return
                            } else {
                                tableLayout.removeAllViews()
                                for (k in 0 until rows) {
                                    val view1 = layoutInflater.inflate(
                                        R.layout.simple_regression_rows,
                                        null
                                    ) as TableRow
                                    tableLayout.addView(view1)
                                    val row1 = sheet.getRow(k)
                                    for (j in 0 until 2) {
                                        val value = getCellAsString(row1, j, formulaevaluator)
                                        val et = view1.getChildAt(j) as EditText
                                        et.setText(value)
                                    }
                                }
                            }
                        }
                    } catch (e: IOException) {
                        Toast.makeText(
                            this,
                            "Error occurred while opening the file",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: FileNotFoundException) {
                        Toast.makeText(this, "File not found", Toast.LENGTH_SHORT)
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
                Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()
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

    fun askForPermission() {
        val permi = Array<String>(1, { i -> "" })
        permi[0] = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Storage permission required")
            dialog.setMessage("Storage permission required to import excel files")
            dialog.setPositiveButton("Ok") { text, listener ->
                ActivityCompat.requestPermissions(this, permi, 1)
            }
            dialog.setNegativeButton("Cancel") { text, listener -> }
        } else {
            ActivityCompat.requestPermissions(this, permi, 1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (!(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}

