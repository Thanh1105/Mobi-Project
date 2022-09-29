package com.example.midterm.finalmobileproject.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.midterm.finalmobileproject.R
import com.example.midterm.finalmobileproject.viewmodel.Utils
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var auth: FirebaseAuth
    private lateinit var context: Context
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private var phone_number: String = "+84000000000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initAction()
        context = this
    }

    private fun initAction() {

        verificationCallbacks()
        clickConfirm()
        auth = FirebaseAuth.getInstance()
        btn_back_to_phone_number.setOnClickListener {
            otp_verification_layout.visibility = View.INVISIBLE
            closeKeyboard()
        }
        btn_otp_verify.setOnClickListener {
            closeKeyboard()
            if (txt_otp.text.toString().length == 6 && storedVerificationId.isNotEmpty())
                verifyCode(txt_otp.text.toString())
        }

        imageView4.setOnClickListener {
            closeKeyboard()
        }
        layout_sign_up.setOnClickListener {
            closeKeyboard()
        }
    }

    private fun clickConfirm() {


        btn_login.setOnClickListener {
            closeKeyboard()
            if (TextUtils.isEmpty(txt_phone_number_sign_up.text.toString())) {
                Toast.makeText(context, getString(R.string.enter_number), Toast.LENGTH_LONG).show()
            } else {

                phone_number = "+84" + txt_phone_number_sign_up.text.toString()
                sendVerificationCode(phone_number)


                try {
                    layout_wait_login.visibility = View.VISIBLE
                } catch (e: Exception) {

                }
                Handler().postDelayed({
                    try {
                        layout_wait_login.visibility = View.INVISIBLE
                    } catch (e: Exception) {

                    }
                }, 2000)
                otp_verification_layout.visibility = View.VISIBLE
            }
        }
    }

    private fun verificationCallbacks() {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(
                s: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                storedVerificationId = s
                resendToken = token
            }
        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        auth.setLanguageCode("vi")
        PhoneAuthProvider.verifyPhoneNumber(options)
        Utils.showToast(context, getString(R.string.send_sucess))
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signInWithPhoneAuthCredential(credential)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {


                    val i = Intent(this, SignUpProfileActivity::class.java)
                    i.putExtra("phonenumber", phone_number)
                    startActivity(i)
                    finish()
                } else {
                    // Sign in failed, display a message and update the UI

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(context, "Wrong OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val manager: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}