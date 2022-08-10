package com.chlqudco.develop.loginsummary

import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chlqudco.develop.loginsummary.databinding.ActivityFaceBookLogInBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FaceBookLogInActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFaceBookLogInBinding.inflate(layoutInflater) }

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager : CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {

        //변수 초기화
        auth = Firebase.auth
        callbackManager = CallbackManager.Factory.create()

        if (auth.currentUser != null){
            auth.signOut()
        }
        binding.FaceBookResultTextView.text = "로그인 안함"

        binding.FaceBookLogInButton.setPermissions("email")
        binding.FaceBookLogInButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult) {
                val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this@FaceBookLogInActivity) { task ->
                        if (task.isSuccessful){
                            binding.FaceBookResultTextView.text = "로그인 성공"
                        } else {
                            binding.FaceBookResultTextView.text = "로그인 오류 발생"
                        }
                    }
            }
            override fun onCancel() { binding.FaceBookResultTextView.text = "로그인 취소" }
            override fun onError(error: FacebookException) { binding.FaceBookResultTextView.text = "로그인 실패" }
        })

        binding.FaceBookLogInButton.setOnClickListener {
            if (auth.currentUser != null){
                auth.signOut()
                binding.FaceBookResultTextView.text = "로그인 안됨"
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager.onActivityResult(requestCode, resultCode, data)

    }
}