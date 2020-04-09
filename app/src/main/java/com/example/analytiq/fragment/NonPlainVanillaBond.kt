package com.example.analytiq.fragment


import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.analytiq.R
import android.widget.Toast
import android.text.TextUtils
import android.widget.EditText
import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileNotFoundException
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat


class NonPlainVanillaBond : Fragment() {

    lateinit var ytm: EditText
    lateinit var spv: EditText
    lateinit var tableLayout: TableLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_non_plain_vanilla_bond, container, false)

        view.findViewById<ScrollView>(R.id.non_plain_vanilla_scroll).isHorizontalScrollBarEnabled =
            false
        view.findViewById<ScrollView>(R.id.non_plain_vanilla_scroll).isVerticalScrollBarEnabled =
            false

        tableLayout = view.findViewById(R.id.table)
        val totalCashOutFlow: EditText = view.findViewById(R.id.tco)
        spv = view.findViewById(R.id.spv)
        ytm = view.findViewById(R.id.ytm)
        val addRow: Button = view.findViewById(R.id.addRow)
        val importBtn: Button = view.findViewById(R.id.import_btn)
        val calculate: Button = view.findViewById(R.id.calculate)

        for (i in 0..3) {
            val tableRow =
                layoutInflater.inflate(R.layout.non_plain_vanilla_bond_rows, null) as TableRow
            tableLayout.addView(tableRow)
        }

        addRow.setOnClickListener {
            val tableRow = layoutInflater.inflate(R.layout.npv_irr_rows, null) as TableRow
            tableLayout.addView(tableRow)
        }

        calculate.setOnClickListener {
            val decimalFormat = DecimalFormat("#.###")

            val yearS = ArrayList<String>()
            val cashinflowS = ArrayList<String>()

            if (!TextUtils.isEmpty(spv.text.toString())) {
                val Spv = java.lang.Double.parseDouble(spv.text.toString())

                for (i in 0 until tableLayout.childCount) {
                    val tableRow = tableLayout.getChildAt(i) as TableRow
                    val year = tableRow.getChildAt(0) as EditText
                    val cashInflow = tableRow.getChildAt(1) as EditText

                    if (!TextUtils.isEmpty(year.text) && !TextUtils.isEmpty(cashInflow.text)) {
                        yearS.add(year.text.toString())
                        cashinflowS.add(cashInflow.text.toString())
                    } else {
                        if (!TextUtils.isEmpty(year.text) || !TextUtils.isEmpty(cashInflow.text)) {
                            break
                        }
                    }
                }

                val yTM = YTM(Spv, cashinflowS, yearS)

                if (yTM == 300.0) {
                    ytm.setText(">100")
                } else if (yTM == 200.0) {
                    ytm.setText("<0")
                } else {
                    ytm.setText(yTM.toString())

                    for (i in 0 until tableLayout.childCount) {
                        val tableRow = tableLayout.getChildAt(i) as TableRow
                        val year = tableRow.getChildAt(0) as EditText
                        val cashInflow = tableRow.getChildAt(1) as EditText
                        val presentValue = tableRow.getChildAt(2) as EditText

                        val pv: Double
                        val cif: Double
                        val t: Double

                        if (!TextUtils.isEmpty(year.text) && !TextUtils.isEmpty(cashInflow.text)) {

                            cif = java.lang.Double.parseDouble(cashInflow.text.toString())
                            t = java.lang.Double.parseDouble(year.text.toString())

                            pv = cif / Math.pow(1 + yTM / 100, t)

                            presentValue.setText((decimalFormat.format(pv)).toString())

                        } else {
                            if (!TextUtils.isEmpty(year.text) || !TextUtils.isEmpty(cashInflow.text)) {
                                Toast.makeText(context, "Fill Properly", Toast.LENGTH_SHORT).show()
                                break
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(context, "Fill ∑ PV", Toast.LENGTH_SHORT).show()
            }
        }

        importBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("*/*")
            startActivityForResult(intent, 20)
        }

        return view
    }

    fun YTM(A: Double, amounts: List<String>, time: List<String>): Double {

        val MAX_LIMIT = 100000
        var ytm = 300.0

        for (r in 0 until MAX_LIMIT + 1) {

            var fr = 0.0
            for (i in amounts.indices) {
                val fri = java.lang.Double.parseDouble(amounts[i]) / Math.pow(
                    1 + java.lang.Double.parseDouble(
                        (r / java.lang.Double.parseDouble(
                            "100000"
                        )).toString()
                    ), java.lang.Double.parseDouble(
                        time[i]
                    )
                )
                fr += fri
            }

            if (fr == A) {
                ytm =
                    java.lang.Double.parseDouble(r.toString()) / java.lang.Double.parseDouble("1000")
                break
            } else if (fr < A) {

                if (r == 0) {
                    if (A - fr < java.lang.Double.parseDouble("0.001")) {
                        ytm =
                            java.lang.Double.parseDouble(r.toString()) / java.lang.Double.parseDouble(
                                "1000"
                            )
                    } else {
                        ytm = 200.0
                    }
                } else {
                    ytm =
                        java.lang.Double.parseDouble(r.toString()) / java.lang.Double.parseDouble("1000")
                }
                break
            }

        }

        return if (ytm == 200.0) {
            200.0
        } else {
            java.lang.Double.parseDouble(ytm.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 20) {
            if (resultCode == RESULT_OK) {
                val data1 = data?.data
                val path = data1?.path

                if (path != null) {
                    if (path.endsWith(".xlsx")) {
                        try {
                            val inputStream = activity?.contentResolver?.openInputStream(data1)
                            val excelfile = XSSFWorkbook(inputStream)
                            val sheet = excelfile.getSheetAt(0)
                            val rows = sheet.physicalNumberOfRows
                            val formulaEvaluator =
                                excelfile.creationHelper.createFormulaEvaluator()

                            for (i in 0 until rows) {
                                val row = sheet.getRow(i)
                                val count = row.physicalNumberOfCells
                                if (count > 2) {
                                    Toast.makeText(
                                        activity as Context,
                                        "Number of columns is more than 3",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return
                                }
                            }
                            tableLayout.removeAllViews()

                            for (i in 0 until rows) {

                                val tableRow = layoutInflater.inflate(
                                    R.layout.non_plain_vanilla_bond_rows,
                                    null
                                ) as TableRow
                                tableLayout.addView(tableRow)

                                val row = sheet.getRow(i)
                                val count = row.physicalNumberOfCells

                                for (j in 0 until count) {
                                    val value = getCellAsString(row, j, formulaEvaluator)
                                    val edit = tableRow.getChildAt(j) as EditText
                                    edit.setText(value)
                                }
                            }
                        } catch (e: FileNotFoundException) {
                            Toast.makeText(
                                activity as Context,
                                "File Not Found",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } catch (e: IOException) {
                            Toast.makeText(
                                activity as Context,
                                "Error reading input stream",
                                Toast.LENGTH_SHORT
                            )
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
            }
        }
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
}
