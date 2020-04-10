package com.example.analytiq.activity

import android.app.Activity
import android.app.AlertDialog
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
import com.jjoe64.graphview.GraphView
import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileNotFoundException
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import com.jjoe64.graphview.series.PointsGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import org.apache.poi.ss.usermodel.Color
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList


class CorrelationActivity : AppCompatActivity() {

    lateinit var calculate: Button
    lateinit var import: Button
    lateinit var addRow: Button
    lateinit var resText: TextView
    lateinit var table: TableLayout
    lateinit var resTable: TableLayout
    lateinit var resTableHead: TableRow
    lateinit var graph: GraphView
    val STORAGE_PERMISSION=1

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
        graph = findViewById(R.id.corr_graph)
        var point_series: PointsGraphSeries<DataPoint>

        for (i in 0..3) {
            val row = layoutInflater.inflate(R.layout.correlation_rows, null) as TableRow
            table.addView(row)
        }

        addRow.setOnClickListener {
            val row = layoutInflater.inflate(R.layout.correlation_rows, null) as TableRow
            table.addView(row)
        }

        calculate.setOnClickListener {

            graph.removeAllSeries()
            graph.viewport.setScrollable(true)
            graph.viewport.setScrollableY(true)
            graph.viewport.isScalable = true
            graph.visibility = View.VISIBLE
            val listX = ArrayList<Double>()
            val listY = ArrayList<Double>()

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
                } else if (!xValue.text.isEmpty() && yValue.text.isEmpty()) {
                    flag = 1
                }
                if (!TextUtils.isEmpty(yValue.text)) {
                    sumY += yValue.text.toString().toDouble()
                    countY++
                } else if (!yValue.text.isEmpty() && xValue.text.isEmpty()) {
                    flag = 1
                }
                if (!TextUtils.isEmpty(xValue.text) && !TextUtils.isEmpty(yValue.text)) {
                    listX.add(xValue.text.toString().toDouble())
                    listY.add(yValue.text.toString().toDouble())
                }
            }
            for (i in 0 until listX.size - 1) {
                for (j in i until listX.size) {
                    if (listX[i] > listX[j]) {
                        var temp = listX[i]
                        listX[i] = listX[j]
                        listX[j] = temp

                        temp = listY[i]
                        listY[i] = listY[j]
                        listY[j] = temp
                    }
                }
            }

            graph.viewport.isXAxisBoundsManual = true
            graph.viewport.setMinX(listX[0])
            graph.viewport.setMaxX(listX[listX.size - 1])
            graph.viewport.isYAxisBoundsManual = true
            graph.viewport.setMinY(Collections.min(listY))
            graph.viewport.setMaxY(Collections.max(listY))


            val dataPoint = Array<DataPoint>(listX.size, { i -> DataPoint(0.0, 0.0) })
            for (k in 0 until listX.size) {
                dataPoint[k] = DataPoint(listX[k], listY[k])
            }

            point_series = PointsGraphSeries(dataPoint)
            graph.addSeries(point_series)
            point_series.setSize(18.0f)
            point_series.setColor(android.graphics.Color.RED)
            //                arrayOf<DataPoint>(
//                    DataPoint(0.0, 2000.0),
//                    DataPoint(1.0, 2500.0),
//                    DataPoint(2.0, 2700.0),
//                    DataPoint(3.0, 3000.0),
//                    DataPoint(4.0, 3500.0),
//                    DataPoint(5.0, 2800.0),
//                    DataPoint(6.0, 3700.0),
//                    DataPoint(7.0, 3800.0),
//                    DataPoint(8.0, 3500.0)
//                )

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
                                        ).show()
                                        return
                                    }
                                }
                                table.removeAllViews()

                                for (i in 0 until rows) {
                                    val view = layoutInflater.inflate(
                                        R.layout.correlation_rows,
                                        null
                                    ) as TableRow
                                    table.addView(view)

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
                this@CorrelationActivity,
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
            val alert=AlertDialog.Builder(this)
            alert.setTitle("Storage permission required")
            alert.setMessage("Storage permission required to import excel files")
            alert.setPositiveButton("Ok") { text, listener ->
                ActivityCompat.requestPermissions(this@CorrelationActivity, permi, STORAGE_PERMISSION)
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
            if(!(grantResults.size>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)){
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
