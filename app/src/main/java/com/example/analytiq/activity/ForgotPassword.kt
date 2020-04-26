package com.example.analytiq.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.analytiq.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {

    lateinit var forgotEmail: EditText
    lateinit var resetbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        forgotEmail = findViewById(R.id.txt_email_forgot)
        resetbtn = findViewById(R.id.forgot_btn)

        resetbtn.setOnClickListener {

            if (this.currentFocus != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
            }

            val entredeEmail = forgotEmail.text.toString()
            if (entredeEmail.isEmpty()) {
//                Toast.makeText(
//                    this@ForgotPassword,
//                    "Please enter your registered email",
//                    Toast.LENGTH_LONG
//                ).show()
                forgotEmail.setError("Please enter your registered email")
            } else if (!Patterns.EMAIL_ADDRESS.matcher(entredeEmail).matches()) {
                Snackbar.make(this.currentFocus!!, "Enter a proper email", Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

                mAuth.sendPasswordResetEmail(entredeEmail).addOnCompleteListener(this) {

                    if (it.isSuccessful) {
                        val intent = Intent(this@ForgotPassword, Login_Form::class.java)
                        startActivity(intent)
                        finish()
                        Snackbar.make(
                            this.currentFocus!!,
                            "Password has been sent to your mail",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        val cseq:CharSequence= it.exception?.message.toString()
                        Snackbar.make(
                            this.currentFocus!!,
                            cseq,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
