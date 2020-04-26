package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.analytiq.R
import java.io.*
import java.text.DecimalFormat


class CriticalValue : AppCompatActivity() {

    lateinit var criticalValue: EditText
    lateinit var probability: EditText
    var table = Array(31) { List(10) { i -> "" } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_critical_value)

        criticalValue = findViewById(R.id.cv)
        probability = findViewById(R.id.p)
        val calcualte: Button = findViewById(R.id.calculate)
        var i = 0
        var br: BufferedReader? = null
        var line = ""
        val cvsSplitBy = ","

        try {
            val `is` = resources.openRawResource(
                resources.getIdentifier(
                    "normaldisttable" /* Name of csv file in raw directory */, "raw",
                    packageName
                )
            ) as InputStream
            br = BufferedReader(InputStreamReader(`is`))

            while (i < 31) {

                line = br.readLine()
                // use comma as separator
                val row =
                    line.split(cvsSplitBy)
                table[i] = row
                i++

            }

        } catch (e: FileNotFoundException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        } finally {
            if (br != null) {
                try {
                    br.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        calcualte.setOnClickListener {
            val decimalFormat = DecimalFormat("#.##")

            if (probability.text.isNotEmpty()) {

                val p = probability.text.toString().toDouble() / 100

                if (p == 0.0) {

                    criticalValue.setText("- " + Character.toString('\u221E'))


                } else if (p == 1.0) {

                    criticalValue.setText(Character.toString('\u221E'))

                } else if (p > 0 && p < 1) {

                    try {
                        val cv = getZ(p)
                        if (cv == 1000.0) {
                            criticalValue.setText("No critical value found in table")
                        } else {
                            criticalValue.setText(decimalFormat.format(cv))
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()

                        criticalValue.setText("No critical value found in table")
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Input a valid number between 0 and 100 ",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                Toast.makeText(this, "Input a valid number between 0 and 100", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun getZ(tail: Double): Double {

        var z = 1000.0

        if (tail == 0.5) {
            return 0.0
        } else if (tail < 0.5) {

            val value = 1 - tail


            if (value > (table[30][9]).toDouble()) {
                throw  Exception("Exceeded provided table limit")
            } else {
                mainloop@ for (i in 0 until table.size) {
                    for (j in 0 until table[0].size) {

                        val compareValue = (table[i][j]).toDouble()

                        if (value == compareValue) {
                            z = -1 * ((i / 10) + ((j.toDouble()) / 100))
                            break@mainloop
                        } else if (value < compareValue) {
                            if (j != 0) {
                                val compareValue0 = (table[i][j - 1]).toDouble()
                                val dif0 = value - compareValue0
                                val dif = compareValue - value
                                if (dif < dif0) {
                                    z = -1 * (((i.toDouble()) / 10) + (j / 100))
                                    break@mainloop

                                } else {
                                    z = -1 * (((i.toDouble()) / 10) + ((j - 1) / 100))
                                    break@mainloop

                                }
                            } else {
                                val compareValue0 =
                                    (table[i - 1][table[i - 1].size - 1]).toDouble()
                                val dif0 = value - compareValue0
                                val dif = compareValue - value
                                if (dif < dif0) {
                                    z = -1 * (((i.toDouble()) / 10) + (j / 100))
                                    break@mainloop

                                } else {
                                    z =
                                        -1 * (((i - 1).toDouble() / 10) + ((table[i - 1].size - 1).toDouble() / 100))
                                    break@mainloop

                                }
                            }
                        }
                    }
                }
            }

        } else if (tail > 0.5) {

            val value = tail

            if (value > (table[30][9]).toDouble()) {
                throw Exception("Exceeded provided table limit")
            } else {
                mainloop@ for (i in 0 until table.size) {
                    for (j in 0 until table[0].size) {

                        val compareValue = (table[i][j]).toDouble()

                        if (value == compareValue) {
                            z = (i.toDouble() / 10) + (j.toDouble() / 100)
                            break@mainloop

                        } else if (value < compareValue) {
                            if (j != 0) {
                                val compareValue0 = (table[i][j - 1]).toDouble()
                                val dif0 = value - compareValue0
                                val dif = compareValue - value
                                if (dif < dif0) {
                                    z = (i.toDouble() / 10) + (j.toDouble() / 100)
                                    break@mainloop

                                } else {
                                    z = (i.toDouble() / 10) + ((j - 1) / 100)
                                    break@mainloop

                                }
                            } else {
                                val compareValue0 =
                                    (table[i - 1][table[i - 1].size - 1]).toDouble()
                                val dif0 = value - compareValue0
                                val dif = compareValue - value
                                if (dif < dif0) {
                                    z = (i.toDouble() / 10) + (j / 100)
                                    break@mainloop

                                } else {
                                    z =
                                        (((i - 1).toDouble()) / 10) + (((table[i - 1].size - 1)) / 100)
                                    break@mainloop

                                }
                            }
                        }
                    }
                }
            }
        }
        return z
    }
}
