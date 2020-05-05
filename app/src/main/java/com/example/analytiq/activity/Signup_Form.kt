package com.example.analytiq.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.analytiq.R
import com.google.android.material.snackbar.Snackbar
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
    lateinit var progress: RelativeLayout


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
        progress = findViewById(R.id.progress)
        progress.visibility = View.GONE

        findViewById<TextView>(R.id.view).setText(Html.fromHtml(String.format(resources.getString(R.string.view_t_c))))

        findViewById<TextView>(R.id.view).setOnClickListener {
            startActivity(Intent(this, TandCActivity::class.java))
        }

        btn_register.setOnClickListener {


            if (this.currentFocus != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            }

            val name = txtname.text.toString().trim()
            val email = txtmail.text.toString().trim()
            val password = txtpasswrd.text.toString().trim()
            val confirm_password = txtcpassword.text.toString().trim()

            if (TextUtils.isEmpty(name)) {
//                Toast.makeText(this@Signup_Form, "Please Enter Name", Toast.LENGTH_SHORT).show()
                txtname.setError("Please enter name")
            } else if (TextUtils.isEmpty(email)) {
//                Toast.makeText(this@Signup_Form, "Please Enter Email", Toast.LENGTH_SHORT).show()
                txtmail.setError("Please enter email")
            } else if (TextUtils.isEmpty(password)) {
//                Toast.makeText(this@Signup_Form, "Please Enter Password", Toast.LENGTH_SHORT).show()
                txtpasswrd.setError("Please enter password")
            } else if (TextUtils.isEmpty(confirm_password)) {
//                Toast.makeText(this@Signup_Form, "Please Enter Password", Toast.LENGTH_SHORT).show()
                txtcpassword.setError("Please confirm password")
            } else if (password.length < 6) {
                Snackbar.make(this.currentFocus!!, "Password Too Short", Snackbar.LENGTH_SHORT)
                    .show()
            } else if (!password.equals(confirm_password)) {
                Snackbar.make(
                    this.currentFocus!!,
                    "Confirm password does not match with the password",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Snackbar.make(
                    this.currentFocus!!,
                    "Please enter proper email-id",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            } else if (!boxx.isChecked) {
                Snackbar.make(
                    this.currentFocus!!,
                    "Please accept terms and conditions",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                progress.visibility = View.VISIBLE
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener(this) {
                            if (it.isSuccessful) {
                                val intent = Intent(this@Signup_Form, Login_Form::class.java)
                                startActivity(intent)
                                finish()
                                progress.visibility = View.GONE
                                Toast.makeText(
                                    this,
                                    "Successfully registered. Please verify your email.",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                val cseq: CharSequence = it.exception?.message.toString()
                                progress.visibility = View.GONE
                                Snackbar.make(
                                    this.currentFocus!!,
                                    cseq,
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    } else {
                        val cseq: CharSequence = it.exception?.message.toString()
                        progress.visibility = View.GONE
                        Snackbar.make(
                            this.currentFocus!!,
                            cseq,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    fun onCheckboxClicked(view: View) {
        val checked = (view as CheckBox).isChecked
    }
}
