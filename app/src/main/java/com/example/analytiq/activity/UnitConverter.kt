package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.*
import com.example.analytiq.R

class UnitConverter : AppCompatActivity(),AdapterView.OnItemSelectedListener {

    var pos = 0
    var lastValue = 0.0
    lateinit var btnConvert:Button
    lateinit var res:TextView

    fun showToast(msg: CharSequence) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun setMsg(msg:String){
        res.setText(msg)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unit_converter)

        val spin: Spinner = findViewById(R.id.unit_spinner)
        btnConvert = findViewById(R.id.Convert)
        val txtEntry: EditText = findViewById(R.id.txtEntry)
        res=findViewById(R.id.result)

        val adapter = ArrayAdapter.createFromResource(
            this, R.array.unit_arrays, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin.adapter = adapter
        spin.setOnItemSelectedListener(this)

        btnConvert.setOnClickListener {
            if (txtEntry.text.toString().trim().length > 0) {
                val textValue = txtEntry.text.toString()
                lastValue = java.lang.Double.parseDouble(textValue)
                val km: Double
                val m: Double
                val cm: Double
                val mm: Double

                if (pos == 0) {
                    km = lastValue / 1000
                    setMsg("$lastValue m(s) = $km km(s)")
                } else if (pos == 1) {
                    m = lastValue * 1000
                    setMsg("$lastValue km(s) = $m m")
                } else if (pos == 2) {
                    cm = lastValue * 100
                    setMsg("$lastValue m(s) = $cm cm(s)")
                } else if (pos == 3) {
                    cm = lastValue * 100000
                    setMsg(lastValue.toString() + "km(s)=" + cm + "cm(s)")
                } else if (pos == 4) {
                    m = lastValue / 100
                    setMsg(lastValue.toString() + "cm(s)=" + m + "m(s)")
                } else if (pos == 5) {
                    km = lastValue / 100000
                    setMsg(lastValue.toString() + "cm(s)=" + km + "km(s)")
                } else if (pos == 6) {
                    mm = lastValue * 10
                    setMsg(lastValue.toString() + "cm(s)=" + mm + "mm(s)")
                } else if (pos == 7) {
                    cm = lastValue / 10
                    setMsg(lastValue.toString() + "mm(s)=" + cm + "cm(s)")
                } else if (pos == 8) {
                    mm = lastValue * 1000
                    setMsg(lastValue.toString() + "m(s)=" + mm + "mm(s)")
                } else {
                    m = lastValue / 1000
                    setMsg(lastValue.toString() + "mm(s)=" + m + "m(s)")
                }
            } else {
                showToast("Please enter a Value")
            }
            res.visibility=View.VISIBLE
        }
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        pos=position
        println("Position is "+position)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        layoutInflater.inflate(R.menu.main_menu,menu)
//        return true
//    }
}
