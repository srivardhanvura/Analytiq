package com.example.analytiq.fragment


import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.opengl.Matrix
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.analytiq.R
import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.w3c.dom.Text
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import kotlin.math.pow


class MultipleRegression : Fragment() {

    lateinit var addBtn: Button
    lateinit var importBtn: Button
    lateinit var calculate: Button
    lateinit var tableLayout: TableLayout
    lateinit var resTable: TableLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_multiple_regression, container, false)

        view.findViewById<ScrollView>(R.id.multiple_regression_scroll)
            .isHorizontalScrollBarEnabled = false
        view.findViewById<ScrollView>(R.id.multiple_regression_scroll).isVerticalScrollBarEnabled =
            false

        addBtn = view.findViewById(R.id.multiple_regression_add)
        importBtn = view.findViewById(R.id.multiple_regression_import)
        calculate = view.findViewById(R.id.multiple_regression_calculate)
        tableLayout = view.findViewById(R.id.multiple_regression_table)
        resTable = view.findViewById(R.id.anova_table)

        for (i in 0..3) {
            val row = layoutInflater.inflate(R.layout.multiple_regression_rows, null) as TableRow
            tableLayout.addView(row)
        }

        addBtn.setOnClickListener {
            val row = layoutInflater.inflate(R.layout.multiple_regression_rows, null) as TableRow
            tableLayout.addView(row)
        }

        calculate.setOnClickListener {
            var c = 0
            for (i in 0 until tableLayout.childCount) {
                val row = tableLayout.getChildAt(i) as TableRow
                val et1 = row.getChildAt(0) as EditText
                val et2 = row.getChildAt(1) as EditText
                val et3 = row.getChildAt(2) as EditText
                if (!et1.text.isEmpty() && !et2.text.isEmpty() && !et3.text.isEmpty())
                    c++
            }
            if (c <= 3) {
                Toast.makeText(
                    activity as Context,
                    "You need to fill more than three rows",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                var numberRows = tableLayout.childCount
                var iter = 0
                val x1Array = ArrayList<Double>()
                val x2Array = ArrayList<Double>()
                val yArray = ArrayList<Double>()
                var x1Mean = 0.0
                var x2Mean = 0.0
                var x1Sum = 0.0
                var x2Sum = 0.0
                var yMean = 0.0
                var ySum = 0.0
                var x1SquareSum = 0.0
                var x2SquareSum = 0.0
                var x1x2Product = 0.0
                var x1ySum = 0.0
                var x2ySum = 0.0
                val yCapArray = ArrayList<Double>()
                val absolonArray = ArrayList<Double>()

                while (iter < numberRows) {
                    val row = tableLayout.getChildAt(iter) as TableRow
                    val et1 = row.getChildAt(0) as EditText
                    val et2 = row.getChildAt(1) as EditText
                    val et3 = row.getChildAt(2) as EditText
                    if (et1.text.isEmpty() || et2.text.isEmpty() || et3.text.isEmpty()) {
                        tableLayout.removeView(tableLayout.getChildAt(iter))
                        numberRows--
                    } else {
                        iter++
                        x1Array.add(et1.text.toString().toDouble())
                        x2Array.add(et2.text.toString().toDouble())
                        yArray.add(et3.text.toString().toDouble())
                    }
                }

                for (i in 0 until x1Array.size) {
                    x1Mean += x1Array[i]
                    x2Mean += x2Array[i]
                    yMean += yArray[i]

                    x1SquareSum += x1Array[i].pow(2)
                    x2SquareSum += x2Array[i].pow(2)
                    x1x2Product += (x1Array[i] * x2Array[i])
                    x1ySum += (x1Array[i] * yArray[i])
                    x2ySum += (x2Array[i] * yArray[i])
                }
                x1Sum = x1Mean
                x2Sum = x2Mean
                ySum = yMean
                x1Mean /= numberRows
                x2Mean /= numberRows
                yMean /= numberRows

                val dArray = Array(3) { Array(3) { i -> 0.0 } }
                dArray[0][0] = numberRows.toDouble()
                dArray[0][1] = x1Sum
                dArray[0][2] = x2Sum
                dArray[1][0] = x1Sum
                dArray[1][1] = x1SquareSum
                dArray[1][2] = x1x2Product
                dArray[2][0] = x2Sum
                dArray[2][1] = x1x2Product
                dArray[2][2] = x2SquareSum

                val d1Array = Array(3) { Array(3) { i -> 0.0 } }
                val d2Array = Array(3) { Array(3) { i -> 0.0 } }
                val d3Array = Array(3) { Array(3) { i -> 0.0 } }

                d1Array[0][0] = ySum
                d1Array[0][1] = x1Sum
                d1Array[0][2] = x2Sum
                d1Array[1][0] = x1ySum
                d1Array[1][1] = x1SquareSum
                d1Array[1][2] = x1x2Product
                d1Array[2][0] = x2ySum
                d1Array[2][1] = x1x2Product
                d1Array[2][2] = x2SquareSum

                d2Array[0][0] = numberRows.toDouble()
                d2Array[0][1] = ySum
                d2Array[0][2] = x2Sum
                d2Array[1][0] = x1Sum
                d2Array[1][1] = x1ySum
                d2Array[1][2] = x1x2Product
                d2Array[2][0] = x2Sum
                d2Array[2][1] = x2ySum
                d2Array[2][2] = x2SquareSum


                d3Array[0][0] = numberRows.toDouble()
                d3Array[0][1] = x1Sum
                d3Array[0][2] = ySum
                d3Array[1][0] = x1Sum
                d3Array[1][1] = x1SquareSum
                d3Array[1][2] = x1ySum
                d3Array[2][0] = x2Sum
                d3Array[2][1] = x1x2Product
                d3Array[2][2] = x2ySum

                val det = detMatrix(dArray[0], dArray[1], dArray[2])
                val det1 = detMatrix(d1Array[0], d1Array[1], d1Array[2])
                val det2 = detMatrix(d2Array[0], d2Array[1], d2Array[2])
                val det3 = detMatrix(d3Array[0], d3Array[1], d3Array[2])

                val b0 = det1 / det
                val b1 = det2 / det
                val b2 = det3 / det


                for (i in 0 until x1Array.size) {
                    yCapArray.add(b0 + (b1 * x1Array[i]) + (b2 * x2Array[i]))
                    absolonArray.add(yArray[i] - yCapArray[i])
                }
                var absolonSquareSum = 0.0
                var ssRegressionSum = 0.0
                for (i in 0 until numberRows) {
                    val row = tableLayout.getChildAt(i) as TableRow
                    val absolon = row.getChildAt(3) as EditText
                    absolonSquareSum += absolonArray[i].pow(2)
                    absolon.setText("%.3f".format(absolonArray[i].pow(2)))
                    ssRegressionSum += (yCapArray[i] - yMean).pow(2)
                }
                var numer = 0.0
                var denom = 0.0
                for (i in 0 until x1Array.size) {
                    numer += (yCapArray[i] - yMean).pow(2)
                    denom += (yArray[i] - yMean).pow(2)
                }
                val rsquare = numer / denom

                val row1 = resTable.getChildAt(1) as TableRow
                val ssRegression = row1.getChildAt(2) as TextView
                val msr = row1.getChildAt(3) as TextView
                ssRegression.setText("%.2f".format(ssRegressionSum))
                msr.setText("%.2f".format((ssRegressionSum / 2)))

                val row2 = resTable.getChildAt(2) as TableRow
                val r2c1 = row2.getChildAt(1) as TextView
                val r2c2 = row2.getChildAt(2) as TextView
                val r2c3 = row2.getChildAt(3) as TextView
                r2c1.setText((numberRows - 3).toString())
                r2c2.setText("%.2f".format(absolonSquareSum))
                r2c3.setText("%.2f".format(absolonSquareSum / (numberRows - 3)))

                val row3 = resTable.getChildAt(3) as TableRow
                val r3c1 = row3.getChildAt(1) as TextView
                val r3c2 = row3.getChildAt(2) as TextView
                r3c1.setText((numberRows - 1).toString())
                r3c2.setText("%.2f".format(ssRegressionSum + absolonSquareSum))

                val adjrsquare = 1 - (((numberRows - 1) / (numberRows - 3)) * (1 - rsquare))

                resTable.visibility = View.VISIBLE
                view.findViewById<TableRow>(R.id.anova_table_head).visibility = View.VISIBLE

                view.findViewById<TextView>(R.id.rsquare_res)
                    .setText("Coefficient of determination: " + "%.3f".format(rsquare))

                view.findViewById<TextView>(R.id.fValue_res)
                    .setText("F-static: " + "%.3f".format((ssRegressionSum / 2) / (absolonSquareSum / (numberRows - 3))))

                view.findViewById<TextView>(R.id.adj_rsquare_res)
                    .setText("Adjusted Coefficient of determination:\n" + "%.3f".format(adjrsquare))

                view.findViewById<TextView>(R.id.model_res)
                    .setText(
                        "Model equation:\n y=(" + "%.2f".format(b0) + ")+(" + "%.2f".format(b1) + ").x1+(" + "%.2f".format(
                            b2
                        ) + ").x2"
                    )

                view.findViewById<LinearLayout>(R.id.res_layout).visibility = View.VISIBLE
            }
        }
        importBtn.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    activity as Context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("*/*")
                startActivityForResult(intent, 40)
            } else {
                askForPermission()
            }
        }

        return view
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
                Toast.makeText(activity as Context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 40 && resultCode == RESULT_OK) {
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
                            if (row.physicalNumberOfCells > 3) {
                                Toast.makeText(
                                    activity as Context,
                                    "Number of columns are more than 3",
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

    fun detMatrix(row1: Array<Double>, row2: Array<Double>, row3: Array<Double>): Double {
        return (row1[0] * ((row2[1] * row3[2]) - (row2[2] * row3[1]))) - (row1[1] * ((row2[0] * row3[2]) - (row3[0] * row2[2]))) + (row1[2] * ((row2[0] * row3[1]) - (row2[1] * row3[0])))
    }
}
