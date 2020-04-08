package com.example.analytiq.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.analytiq.R
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
            val entredeEmail = forgotEmail.text.toString()
            if (entredeEmail.isEmpty()) {
                Toast.makeText(
                    this@ForgotPassword,
                    "Please enter your registered email",
                    Toast.LENGTH_LONG
                ).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(entredeEmail).matches()) {
                Toast.makeText(this@ForgotPassword, "Enter a proper email", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

                mAuth.sendPasswordResetEmail(entredeEmail).addOnCompleteListener(this) {

                    if (it.isSuccessful) {
                        val intent = Intent(this@ForgotPassword, Login_Form::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(
                            this@ForgotPassword,
                            "Password has been sent to your mail",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@ForgotPassword,
                            it.exception?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
