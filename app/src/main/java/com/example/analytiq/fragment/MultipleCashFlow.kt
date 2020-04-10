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
import com.example.analytiq.R
import android.text.TextUtils
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileNotFoundException
import java.io.IOException
//import com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table
import java.text.DecimalFormat
import java.text.SimpleDateFormat


class MultipleCashFlow : Fragment() {

    lateinit var tableLayout: TableLayout
    lateinit var totalPresentValueTV: TextView
    val STORAGE_PERMISSION = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_multiple_cash_flow, container, false)

        view.findViewById<ScrollView>(R.id.multiple_cash_flow).isHorizontalScrollBarEnabled = false
        view.findViewById<ScrollView>(R.id.multiple_cash_flow).isVerticalScrollBarEnabled = false

        tableLayout = view.findViewById(R.id.table)
        totalPresentValueTV = view.findViewById(R.id.total_present_value)
        val addRow = view.findViewById<Button>(R.id.addRow)
        val calculate = view.findViewById<Button>(R.id.calculate)

        for (i in 0..3) {
            val tableRow =
                layoutInflater.inflate(R.layout.multiple_cash_flow_rows, null) as TableRow
            tableLayout.addView(tableRow)
            //            TextView sno = (TextView) tableRow.getChildAt(0);
            //            sno.setText(String.valueOf(tableLayout.getChildCount()));

        }
        view.findViewById<Button>(R.id.importBtn).setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    activity as Context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                askStorgaePermission()
            } else {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("*/*")
                startActivityForResult(Intent.createChooser(intent, "Choose a file"), 10)
            }
        }

        addRow.setOnClickListener {
            val tableRow =
                layoutInflater.inflate(R.layout.multiple_cash_flow_rows, null) as TableRow
            tableLayout.addView(tableRow)
            //                TextView sno = (TextView) tableRow.getChildAt(0);
            //                sno.setText(String.valueOf(tableLayout.getChildCount()));
        }

        calculate.setOnClickListener {
            val decimalFormat = DecimalFormat("#.###")

            var totalPresentValue = 0.0

            for (i in 0 until tableLayout.childCount) {
                val tableRow = tableLayout.getChildAt(i) as TableRow
                val amount = tableRow.getChildAt(0) as EditText
                val timeInterval = tableRow.getChildAt(1) as EditText
                val interestRate = tableRow.getChildAt(2) as EditText
                val presentValue = tableRow.getChildAt(3) as EditText

                val pv: Double
                val a: Double
                val t: Double
                val r: Double

                if (!TextUtils.isEmpty(amount.text) && !TextUtils.isEmpty(timeInterval.text) && !TextUtils.isEmpty(
                        interestRate.text
                    )
                ) {
                    a = java.lang.Double.parseDouble(amount.text.toString())
                    t = java.lang.Double.parseDouble(timeInterval.text.toString())
                    r = java.lang.Double.parseDouble(interestRate.text.toString())

                    pv = a / Math.pow(1 + r / 100, t)

                    totalPresentValue += pv

                    presentValue.setText((decimalFormat.format(pv)).toString())

                } else {
                    if (!(TextUtils.isEmpty(amount.text) && TextUtils.isEmpty(timeInterval.text) && TextUtils.isEmpty(
                            interestRate.text
                        ))
                    ) {
                        Toast.makeText(context, "Fill Properly", Toast.LENGTH_SHORT).show()
                        break
                    }
                }

                totalPresentValueTV.text =
                    "∑ of Present Value = ₹ " + decimalFormat.format(totalPresentValue)
            }
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {

            10 -> {
                if (resultCode == RESULT_OK) {
                    val dataUri = data?.data
                    val path = data?.data?.path
                    if (path != null) {
                        if (path.endsWith(".xlsx", true)) {
                            try {
                                val inputStream = dataUri?.let {
                                    activity?.contentResolver?.openInputStream(
                                        it
                                    )
                                }
                                val excelfile = XSSFWorkbook(inputStream)
                                val sheet = excelfile.getSheetAt(0)
                                val rows = sheet.physicalNumberOfRows
                                val formulaEvaluator =
                                    excelfile.creationHelper.createFormulaEvaluator()

                                for (i in 0 until rows) {
                                    val row = sheet.getRow(i)
                                    val cellCount = row.physicalNumberOfCells
                                    if (cellCount > 3) {
                                        Toast.makeText(
                                            activity as Context,
                                            "Excel file not in the required format",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        break
                                    }
                                }

                                tableLayout.removeAllViews()

                                for (i in 0 until rows) {
                                    val tableRow = layoutInflater.inflate(
                                        R.layout.multiple_cash_flow_rows,
                                        null
                                    ) as TableRow
                                    tableLayout.addView(tableRow)

                                    val row = sheet.getRow(i)
                                    val cellCount = row.physicalNumberOfCells

                                    for (j in 0 until 3) {
                                        val value = getCellAsString(row, j, formulaEvaluator)
                                        val edit = tableRow.getChildAt(j) as EditText
                                        edit.setText(value)
                                    }
                                }
                            } catch (e: IOException) {
                                Toast.makeText(
                                    activity as Context,
                                    "Error reading input stream",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } catch (e: FileNotFoundException) {
                                Toast.makeText(
                                    activity as Context,
                                    "File Not Found",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Select only excel files",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
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

    fun askStorgaePermission() {
        val permi = Array<String>(1, { i -> "" })
        permi[0] = android.Manifest.permission.READ_EXTERNAL_STORAGE

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this.requireActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            val alert = AlertDialog.Builder(activity as Context)
            alert.setTitle("Storage permission required")
            alert.setMessage("Storage permission required to import excel files")
            alert.setPositiveButton("Ok") { text, listener ->
                ActivityCompat.requestPermissions(this.requireActivity(), permi, STORAGE_PERMISSION)
            }
            alert.setNegativeButton("Cancel") { text, listener -> }
            alert.create()
            alert.show()
        } else {
            ActivityCompat.requestPermissions(this.requireActivity(), permi, STORAGE_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == STORAGE_PERMISSION) {
            if (!(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(activity as Context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
