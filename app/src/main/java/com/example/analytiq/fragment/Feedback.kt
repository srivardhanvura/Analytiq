package com.example.analytiq.fragment


import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.MailTo
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.example.analytiq.R

class Feedback : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feedback, container, false)

        view.findViewById<Button>(R.id.btn).setOnClickListener {

            if (view.findViewById<EditText>(R.id.et).text.isEmpty())
                Toast.makeText(activity as Context, "Fill properly", Toast.LENGTH_SHORT).show()
            else {
                val msg = view.findViewById<EditText>(R.id.et).text.toString()
                val email=arrayOf("analytiqapp@gmail.com")

                val intent=Intent(Intent.ACTION_SENDTO).apply {
                    data= Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL,email)
                    putExtra(Intent.EXTRA_SUBJECT,"Feedback related to analytiq app")
                    putExtra(Intent.EXTRA_TEXT,msg)
                }
                try{
                    startActivity(Intent.createChooser(intent,"Choose an email client app"))
                }catch (e:ActivityNotFoundException){
                    Toast.makeText(activity as Context,"This device does not have any email clients",Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }


}
