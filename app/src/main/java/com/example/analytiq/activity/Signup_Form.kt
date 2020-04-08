package com.example.analytiq.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.*
import com.example.analytiq.R
import com.google.firebase.auth.FirebaseAuth


class Signup_Form : AppCompatActivity() {

    lateinit var txtname: EditText
    lateinit var number: EditText
    lateinit var txtmail: EditText
    lateinit var txtpasswrd: EditText
    lateinit var txtcpassword: EditText
    lateinit var btn_register: Button
    lateinit var boxx: CheckBox
    lateinit var mAuth: FirebaseAuth
    lateinit var progress:RelativeLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_form)

        txtname = findViewById(R.id.txt_name)
        txtmail = findViewById(R.id.txt_mail)
        txtpasswrd = findViewById(R.id.txt_passwrd)
        txtcpassword = findViewById(R.id.txt_cpassword)
        btn_register = findViewById(R.id.btn)
        boxx = findViewById(R.id.box)
        mAuth = FirebaseAuth.getInstance()
        progress=findViewById(R.id.progress)
        progress.visibility=View.GONE


        btn_register.setOnClickListener {

            val name = txtname.text.toString().trim()
            val email = txtmail.text.toString().trim()
            val password = txtpasswrd.text.toString().trim()
            val confirm_password = txtcpassword.text.toString().trim()

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this@Signup_Form, "Please Enter Name", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(email)) {
                Toast.makeText(this@Signup_Form, "Please Enter Email", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(this@Signup_Form, "Please Enter Password", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(confirm_password)) {
                Toast.makeText(this@Signup_Form, "Please Enter Password", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this@Signup_Form, "Password Too Short", Toast.LENGTH_SHORT).show()
            }  else if (!password.equals(confirm_password)) {
                Toast.makeText(
                    this@Signup_Form,
                    "Confirm password does not match with the password",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this@Signup_Form, "Please enter proper email-id", Toast.LENGTH_SHORT)
                    .show()
            } else if (!boxx.isChecked) {
                Toast.makeText(
                    this@Signup_Form,
                    "Please accept terms and conditions",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                progress.visibility=View.VISIBLE
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener(this) {
                            if (it.isSuccessful) {
                                val intent = Intent(this@Signup_Form, Login_Form::class.java)
                                startActivity(intent)
                                finish()
                                progress.visibility=View.GONE
                                Toast.makeText(
                                    this@Signup_Form,
                                    "Successfully registered. Please verify your email.",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                progress.visibility=View.GONE
                                Toast.makeText(
                                    this@Signup_Form,
                                    it.exception?.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } else {
                        progress.visibility = View.GONE
                        Toast.makeText(this@Signup_Form, it.exception?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

    }

    fun onCheckboxClicked(view: View) {
        val checked = (view as CheckBox).isChecked
    }
}
