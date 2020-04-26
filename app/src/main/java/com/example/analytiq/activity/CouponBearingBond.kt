package com.example.analytiq.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
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

class CouponBearingBond : AppCompatActivity() {

    lateinit var tableLayout: TableLayout
    lateinit var calculateStr: LinearLayout
    val STORAGE_PERMISSION=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon_bearing_bond)

        tableLayout = findViewById(R.id.table)
        calculateStr =findViewById(R.id.calculate_str)

        for (i in 0 until 4) {
            val tableRow =
                getLayoutInflater().inflate(R.layout.coupon_bearing_bond_rows, null) as TableRow
            tableLayout.addView(tableRow)
            val duration = tableRow.getChildAt(0) as EditText
            val coupon = tableRow.getChildAt(2) as EditText
            duration.setText((tableLayout.getChildCount()).toString())
            if (i == 0) {
                coupon.setText("0")
            }
        }

        findViewById<Button>(R.id.addRow).setOnClickListener {
            val tableRow =
                getLayoutInflater().inflate(R.layout.coupon_bearing_bond_rows, null) as TableRow
            tableLayout.addView(tableRow)
            val duration = tableRow.getChildAt(0) as EditText
            duration.setText((tableLayout.getChildCount()).toString())
        }

        findViewById<Button>(R.id.import_btn).setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                askStoragePermission()
            } else {

                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("*/*")
                startActivityForResult(intent, 15)
            }
        }

        findViewById<Button>(R.id.calculate).setOnClickListener {
            calculateStr.removeAllViews()
            for (i in 0 until tableLayout.childCount) {
                val tableRow = tableLayout.getChildAt(i) as TableRow
                val rate = tableRow.getChildAt(4) as EditText
                rate.getText().clear()
            }
            val decimalFormat = DecimalFormat("#.###")

            for (i in 0 until tableLayout.childCount) {
                val tableRow = tableLayout.getChildAt(i) as TableRow
                val period = tableRow.getChildAt(0) as EditText
                val PV = tableRow.getChildAt(1) as EditText
                val coupon = tableRow.getChildAt(2) as EditText
                val FV = tableRow.getChildAt(3) as EditText
                val rate = tableRow.getChildAt(4) as EditText

                var p: Double
                var pv: Double
                var c: Double
                var fv: Double
                var r: Double

                if (!TextUtils.isEmpty(period.getText()) &&
                    !TextUtils.isEmpty(PV.getText()) &&
                    !TextUtils.isEmpty(coupon.getText()) &&
                    !TextUtils.isEmpty(FV.getText())
                ) {
                    p = (period.getText().toString().toDouble())
                    pv = (PV.getText().toString().toDouble())
                    c = (coupon.getText().toString().toDouble())
                    fv = (FV.getText().toString().toDouble())

                    var constant = 0.0
                    for (j in 0 until i) {
                        val tableRoww = tableLayout.getChildAt(j) as TableRow
                        val ratee = tableRoww.getChildAt(4) as EditText
                        constant += 1 / (1 + ((ratee.getText().toString().toDouble()) / 100))
                    }

                    r = Math.exp(Math.log((fv + c) / (pv - (c * constant))) / p) - 1

                    rate.setText((decimalFormat.format(r * 100)))
                } else {
                    if (!TextUtils.isEmpty(PV.getText()) || !TextUtils.isEmpty(coupon.getText()) || !TextUtils.isEmpty(
                            FV.getText()
                        )
                    ) {
                        Toast.makeText(this, "Fill Properly", Toast.LENGTH_SHORT).show()
                    }
                    break

                }
            }

            for (i in 0 until tableLayout.childCount) {

                val tableRowf = tableLayout.getChildAt(i) as TableRow
                val ratef = tableRowf.getChildAt(4) as EditText
                if (!TextUtils.isEmpty(ratef.getText())) {
                    for (j in (i + 1) until tableLayout.childCount) {
                        val tableRows = tableLayout.getChildAt(j) as TableRow
                        val rates = tableRows.getChildAt(4) as EditText
                        if (!TextUtils.isEmpty(rates.getText())) {
                            val f = Math.pow(
                                Math.pow(
                                    1 + ((rates.getText().toString().toDouble()) / 100),
                                    ((j + 1).toString()).toDouble()
                                ) / Math.pow(
                                    1 + ((ratef.getText().toString().toDouble()) / 100),
                                    ((i + 1).toString()).toDouble()
                                ), 1 / ((j - i).toString()).toDouble()
                            ) - 1

                            val linearLayout = getLayoutInflater().inflate(
                                R.layout.coupon_bearing_bond_sol_str,
                                null
                            ) as LinearLayout
                            calculateStr.addView(linearLayout)
                            val text = linearLayout.getChildAt(0) as TextView
                            val result = linearLayout.getChildAt(1) as EditText

                            text.setText("F" + (i + 1) + (j + 1))
                            result.setText((decimalFormat.format(f * 100)) + " %")
                        } else {
                            break
                        }
                    }

                } else {
                    break
                }

            }
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
                                    if (cellCount > 3) {
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
                                        R.layout.correlation_rows,
                                        null
                                    ) as TableRow
                                    tableLayout.addView(view)

                                    val row = sheet.getRow(i)
                                    val count = row.physicalNumberOfCells

                                    for (j in 1 until count-1) {
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
    fun askStoragePermission(){

        val permi=Array<String>(1,{i->""})
        permi[0]=android.Manifest.permission.READ_EXTERNAL_STORAGE

        if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            val alert= AlertDialog.Builder(this)
            alert.setTitle("Storage permission required")
            alert.setMessage("Storage permission required to import excel files")
            alert.setPositiveButton("Ok") { text, listener ->
                ActivityCompat.requestPermissions(this, permi, STORAGE_PERMISSION)
            }
            alert.setNegativeButton("Cancel") { text, listener -> }
            alert.create()
            alert.show()
        }else{
            ActivityCompat.requestPermissions(this,permi,STORAGE_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==STORAGE_PERMISSION){
            if(!(grantResults.size>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED)){
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

}
