package com.example.analytiq.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.analytiq.R
import com.example.analytiq.fragment.*

class MensurationActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {

    lateinit var frame_layout: FrameLayout
    lateinit var spinner:Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensuration)

        findViewById<ScrollView>(R.id.mensuration_scroll_view).isHorizontalScrollBarEnabled=false
        findViewById<ScrollView>(R.id.mensuration_scroll_view).isVerticalScrollBarEnabled=false

        frame_layout = findViewById(R.id.frame_mensu)
        spinner=findViewById(R.id.mensu_Spinner)

        val list_mensu= ArrayList<String>()
        list_mensu.add("Cube")
        list_mensu.add("Cuboid")
        list_mensu.add("Cylinder")
        list_mensu.add("Cone")
        list_mensu.add("Sphere")

        val arrayAdapter=ArrayAdapter(this@MensurationActivity,R.layout.spinner_item,list_mensu)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter=arrayAdapter
        spinner.setOnItemSelectedListener(this)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selected=parent?.getItemAtPosition(position).toString()

        if(selected.equals("Cube")){
            supportFragmentManager.beginTransaction().replace(R.id.frame_mensu, Cube_Mensuration())
                .commit()
        }else if(selected.equals("Cuboid")){
            supportFragmentManager.beginTransaction().replace(R.id.frame_mensu, Cuboid_Mensuration())
                .commit()
        }
        else if(selected.equals("Cylinder")){
            supportFragmentManager.beginTransaction().replace(R.id.frame_mensu, Cylinder_Mensuration())
                .commit()
        }
        else if(selected.equals("Cone")){
            supportFragmentManager.beginTransaction().replace(R.id.frame_mensu, Cone_Mensuration())
                .commit()
        }
        else if(selected.equals("Sphere")){
            supportFragmentManager.beginTransaction().replace(R.id.frame_mensu, Sphere_Mensuration())
                .commit()
        }
    }
}
