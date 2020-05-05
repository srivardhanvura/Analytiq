package com.example.analytiq.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.analytiq.R
import com.example.analytiq.util.ConnectivityManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Login_Form : AppCompatActivity() {

    lateinit var txtemail: EditText
    lateinit var txtpassword: EditText
    lateinit var btn_login: Button
    lateinit var txtForgot: TextView
    lateinit var btn_registerr: Button
    lateinit var googleSignin: SignInButton
    lateinit var mAuth: FirebaseAuth
    lateinit var gsOption: GoogleSignInOptions
    lateinit var gsClient: GoogleSignInClient
    lateinit var progress: RelativeLayout

    val RC_SIGN_IN: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_form)

        txtemail = findViewById(R.id.txt_email)
        txtpassword = findViewById(R.id.txt_password)
        btn_login = findViewById(R.id.bttn)
        btn_registerr = findViewById(R.id.btnn)
        txtForgot = findViewById(R.id.forgotTxt)
        googleSignin = findViewById(R.id.google_btn)
        mAuth = FirebaseAuth.getInstance()
        progress = findViewById(R.id.progress_login)
        progress.visibility = View.GONE

        gsOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        gsClient = GoogleSignIn.getClient(this@Login_Form, gsOption)

        txtForgot.setOnClickListener {
            val intent = Intent(this@Login_Form, ForgotPassword::class.java)
            startActivity(intent)
        }


        btn_login.setOnClickListener {

            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }

            val enteredemail = txtemail.text.toString().trim()
            val enteredpass = txtpassword.text.toString().trim()

            if (enteredemail.isEmpty()) {
//                Toast.makeText(this@Login_Form, "Enter email", Toast.LENGTH_SHORT).show()
                txtemail.setError("Enter email")
            } else if (enteredpass.isEmpty()) {
//                Toast.makeText(this@Login_Form, "Enter password", Toast.LENGTH_SHORT).show()
                txtpassword.setError("Enter Password")
            } else if (!Patterns.EMAIL_ADDRESS.matcher(enteredemail).matches()) {
//                Toast.makeText(this@Login_Form, "Please enter proper email-id", Toast.LENGTH_SHORT)
//                    .show()
                Snackbar.make(
                    this.currentFocus!!,
                    "Please enter proper email-id",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                mAuth.signInWithEmailAndPassword(enteredemail, enteredpass)
                    .addOnCompleteListener(this) {
                        progress.visibility = View.VISIBLE
                        if (it.isSuccessful) {
                            val user = mAuth.currentUser
                            if (user != null) {
                                if (user.isEmailVerified) {
                                    val intent = Intent(this@Login_Form, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                    progress.visibility = View.GONE
//                                Toast.makeText(
//                                    this@Login_Form,
//                                    "Sign in successful",
//                                    Toast.LENGTH_SHORT
//                                ).show()
                                } else {
                                    progress.visibility = View.GONE
                                    Snackbar.make(
                                        this.currentFocus!!,
                                        "Please verify user email",
                                        Snackbar.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                        } else {
                            progress.visibility = View.GONE
                            val cseq: CharSequence = it.exception?.message.toString()
                            Snackbar.make(
                                this.currentFocus!!,
                                cseq,
                                Snackbar.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
            }
        }

        googleSignin.setOnClickListener {
            if (ConnectivityManager().checkConnectivity(this)) {
                val intent: Intent = gsClient.signInIntent
                startActivityForResult(intent, RC_SIGN_IN)
            }else{
                Toast.makeText(
                    this,
                    "No internet connection",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    fun btn_signupForm(view: View) {
        startActivity(Intent(applicationContext, Signup_Form::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                }
            } catch (e: ApiException) {
                Snackbar.make(this.currentFocus!!, "Google sign in failed:(", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = FirebaseAuth.getInstance().getCurrentUser()
                if (user != null) {
                    val intent = Intent(this@Login_Form, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    progress.visibility = View.GONE
//                    val name = user.getEmail()
//                    Toast.makeText(this@Login_Form, "LoggedIn", Toast.LENGTH_SHORT)
//                        .show()
                }
            } else {
                Snackbar.make(this.currentFocus!!, "Google sign in failed:(", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}
