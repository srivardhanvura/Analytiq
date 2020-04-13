package com.example.analytiq.fragment


import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.analytiq.R
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.PointsGraphSeries
import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow

class SimpleRegression : Fragment() {

    lateinit var addBtn: Button
    lateinit var importBtn: Button
    lateinit var tableLayout: TableLayout
    lateinit var calculate: Button
    lateinit var graph: GraphView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_simple_regression, container, false)

        addBtn = view.findViewById(R.id.simple_Regression_addBtn)
        importBtn = view.findViewById(R.id.simple_Regression_importBtn)
        calculate = view.findViewById(R.id.simple_regression_calc)
        tableLayout = view.findViewById(R.id.simple_regression_table)
        graph = view.findViewById(R.id.simple_regression_graph)


        for (i in 0..3) {
            val row = layoutInflater.inflate(R.layout.simple_regression_rows, null)
            tableLayout.addView(row)
        }

        addBtn.setOnClickListener {
            val row = layoutInflater.inflate(R.layout.simple_regression_rows, null)
            tableLayout.addView(row)
        }

        importBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    activity as Context,
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

        calculate.setOnClickListener {

            var numberRows = tableLayout.childCount
            var xMean = 0.0
            var yMean = 0.0
            var filled = 0
            var flag = 0

            for (i in 0 until numberRows) {
                val row = tableLayout.getChildAt(i) as TableRow
                val x = row.getChildAt(0) as EditText
                val y = row.getChildAt(1) as EditText
                if (!x.text.isNullOrEmpty() && !y.text.isNullOrEmpty()) {
                    flag = 1
                    break
                }
            }
            if (flag == 0) {
                Toast.makeText(activity as Context, "Enter values", Toast.LENGTH_SHORT).show()
            } else {

                for (i in 0 until numberRows) {
                    val row = tableLayout.getChildAt(i) as TableRow
                    val x = row.getChildAt(0) as EditText
                    val y = row.getChildAt(1) as EditText
                    if (x.text.isNotEmpty() && y.text.isNotEmpty()) {
                        xMean += x.text.toString().toDouble()
                        yMean += y.text.toString().toDouble()
                        filled++
                    }
                }
                xMean /= filled
                yMean /= filled

                val thirdCol = ArrayList<Double>()
                val xmeandiff = ArrayList<Double>()
                var numValue = 0.0
                var denomValue = 0.0

                for (i in 0 until numberRows) {
                    val row = tableLayout.getChildAt(i) as TableRow
                    val x = row.getChildAt(0) as EditText
                    val y = row.getChildAt(1) as EditText
                    if (x.text.isNotEmpty() && y.text.isNotEmpty()) {
                        val third = row.getVirtualChildAt(2) as EditText
                        val thirdText = "%.3f".format((y.text.toString().toDouble() - yMean).pow(2))
                        thirdCol.add(thirdText.toDouble())
                        third.setText(thirdText)

                        xmeandiff.add((x.text.toString().toDouble() - xMean))

                        numValue += (y.text.toString().toDouble() - yMean) * (x.text.toString().toDouble() - xMean)
                        denomValue += (x.text.toString().toDouble() - xMean).pow(2)
                    }
                }
                val b1Cap = numValue / denomValue
                val b0Cap = yMean - (b1Cap * xMean)
                val yCap = ArrayList<Double>()

                for (i in 0 until numberRows) {
                    val row = tableLayout.getChildAt(i) as TableRow
                    val x = row.getChildAt(0) as EditText
                    val y = row.getChildAt(1) as EditText
                    if (x.text.isNotEmpty() && y.text.isNotEmpty()) {
                        val value = b0Cap + (b1Cap * x.text.toString().toDouble())
                        yCap.add(value)
                    }
                }

                var j = 0
                val absolon = ArrayList<Double>()
                for (i in 0 until numberRows) {
                    val row = tableLayout.getChildAt(i) as TableRow
                    val x = row.getChildAt(0) as EditText
                    val y = row.getChildAt(1) as EditText
                    if (x.text.isNotEmpty() && y.text.isNotEmpty()) {
                        val fourthCol = row.getChildAt(3) as EditText
                        val yCapValue = yCap[j]
                        val yValue = y.text.toString().toDouble()
                        absolon.add((yValue - yCapValue).pow(2))
                        val fourthText = "%.3f".format((yValue - yCapValue).pow(2))
                        fourthCol.setText(fourthText)
                        j++
                    }
                }
                var see: String
                var rsquare: String
                var absolonSum = 0.0
                var thirdColSum = 0.0

                for (i in 0 until numberRows) {
                    val row = tableLayout.getChildAt(i) as TableRow
                    val x = row.getChildAt(0) as EditText
                    val yval = row.getChildAt(1) as EditText
                    val y = row.getChildAt(2) as EditText
                    if (x.text.isNotEmpty() && yval.text.isNotEmpty()) {
                        val fourthCol = row.getChildAt(3) as EditText
                        absolonSum += fourthCol.text.toString().toDouble()
                        thirdColSum += y.text.toString().toDouble()
                    }
                }
                see = "%.3f".format((absolonSum / (filled - 2)).pow(0.5))
                rsquare = "%.3f".format((1 - (absolonSum / thirdColSum)) * 100) + "%"
                if (rsquare.equals("NaN"))
                    rsquare = "Not available for this data"
                if (see.equals("NaN"))
                    see = "Not available for this data"

                view.findViewById<TextView>(R.id.rsquare_text)
                    .setText("Coefficient of determination: \n" + rsquare)
                view.findViewById<TextView>(R.id.see_text)
                    .setText("Standard Error of the Estimate- \n" + see)

                view.findViewById<TextView>(R.id.rsquare_text).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.see_text).visibility = View.VISIBLE

                var i = 0
                while (i < numberRows) {
                    val row = tableLayout.getChildAt(i) as TableRow
                    val x = row.getChildAt(0) as EditText
                    val y = row.getChildAt(1) as EditText
                    if (x.text.isEmpty() || y.text.isEmpty()) {
                        tableLayout.removeView(tableLayout.getChildAt(i))
                        numberRows--
                    } else {
                        i++
                    }
                }
                val xValues = ArrayList<Double>()
                val yValues = ArrayList<Double>()
                for (i in 0 until numberRows) {
                    val row1 = tableLayout.getChildAt(i) as TableRow
                    val x = row1.getChildAt(0) as EditText
                    val y = row1.getChildAt(1) as EditText
                    xValues.add(x.text.toString().toDouble())
                    yValues.add(y.text.toString().toDouble())
                }

                for (i in 0 until numberRows - 1) {
                    for (j in i until numberRows) {
                        if (xValues[i] > xValues[j]) {
                            var temp = xValues[i]
                            xValues[i] = xValues[j]
                            xValues[j] = temp

                            temp = yValues[i]
                            yValues[i] = yValues[j]
                            yValues[j] = temp
                        }
                    }
                }
                val datapoint = Array<DataPoint>(numberRows, { i -> DataPoint(0.0, 0.0) })
                for (i in 0 until numberRows)
                    datapoint[i] = DataPoint(xValues[i], yValues[i])
                val pointseries = PointsGraphSeries(datapoint)
                graph.removeAllSeries()
                graph.addSeries(pointseries)
                pointseries.setSize(18.0f)
                pointseries.setColor(android.graphics.Color.RED)


                graph.viewport.isXAxisBoundsManual = true
                graph.viewport.isYAxisBoundsManual = true
                graph.viewport.setScrollable(false)
                graph.viewport.setScrollableY(false)
//                graph.viewport.isScalable = true
                graph.viewport.setMinX(Collections.min(xValues))
                graph.viewport.setMaxX(Collections.max(xValues))
                graph.viewport.setMinX(Collections.min(yValues))
                graph.viewport.setMaxY(Collections.max(yValues))

                graph.visibility = View.VISIBLE
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 30 && resultCode == RESULT_OK) {
            val data1 = data?.data
            val path = data1?.path
            if (path != null) {
                if (path.endsWith(".xlsx")) {
                    try {
                        val inputStream = activity?.contentResolver?.openInputStream(data1)
                        val excelFile = XSSFWorkbook(inputStream)
                        val sheet = excelFile.getSheetAt(0)
                        val rows = sheet.physicalNumberOfRows
                        val formulaevaluator = excelFile.creationHelper.createFormulaEvaluator()
                        for (i in 0 until rows) {
                            val row = sheet.getRow(i)
                            if (row.physicalNumberOfCells > 2) {
                                Toast.makeText(
                                    activity as Context,
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
                            activity as Context,
                            "Error occurred while opening the file",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: FileNotFoundException) {
                        Toast.makeText(activity as Context, "File not found", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        activity as Context,
                        "File type not supported",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(activity as Context, "Some error occurred", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(activity as Context, "Some error occurred", Toast.LENGTH_SHORT).show()
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
                activity as Context,
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
                this.requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Storage permission required")
            dialog.setMessage("Storage permission required to import excel files")
            dialog.setPositiveButton("Ok") { text, listener ->
                ActivityCompat.requestPermissions(this.requireActivity(), permi, 1)
            }
            dialog.setNegativeButton("Cancel") { text, listener -> }
        } else {
            ActivityCompat.requestPermissions(this.requireActivity(), permi, 1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (!(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(activity as Context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
