package com.chlqudco.develop.loginsummary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.chlqudco.develop.loginsummary.databinding.ActivityPhoneLogInBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class PhoneLogInActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPhoneLogInBinding.inflate(layoutInflater) }
    private val auth by lazy { Firebase.auth }
    private var verificationId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        //로그인 되어 있으면 로그아웃
        if (auth.currentUser != null){ auth.signOut() }
        binding.PhoneResultTextView.text = "인증 아직 안함"

        //인증번호 요청 버튼
        binding.PhoneRequestCodeButton.setOnClickListener {
            requestCode()
        }

        //인증번호 검증 버튼
        binding.PhoneCheckCodeButton.setOnClickListener {
            checkCode()
        }
    }



    private fun requestCode() {
        val callbacks =  object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {}
            override fun onVerificationFailed(e: FirebaseException) {}
            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                this@PhoneLogInActivity.verificationId = verificationId
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+821071036113")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        auth.setLanguageCode("kr")

    }

    private fun checkCode() {
        val credential = PhoneAuthProvider.getCredential(verificationId, binding.PhoneCodeEditText.text.toString())
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    binding.PhoneResultTextView.text = "인증 성공"
                } else {
                    binding.PhoneResultTextView.text = "인증 실패"
                }
            }
    }

}