package com.chlqudco.develop.loginsummary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.chlqudco.develop.loginsummary.databinding.ActivityEmailLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EmailLogInActivity : AppCompatActivity() {

    private val binding by lazy { ActivityEmailLogInBinding.inflate(layoutInflater) }
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        //로그인결과 텍스트 초기화
        if (auth.currentUser != null){
            successLogIn()
        } else{
            failLogIn()
        }

        //로그인 버튼 클릭
        binding.EmailLogInLogInButton.setOnClickListener {
            //예외처리 따위 안함
            val email = binding.EmailLogInIdEditText.text.toString()
            val password = binding.EmailLogInPasswordEditText.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){task ->
                    if (task.isSuccessful){
                        successLogIn()
                    } else{
                        failLogIn()
                    }
                }
        }

        //로그아우 버튼 클릭
        binding.EmailLogInLogOutButton.setOnClickListener {
            auth.signOut()
            failLogIn()
        }

        //회원가입 버튼 클릭
        binding.EmailLogInSignUpButton.setOnClickListener {
            //예외처리 따위 안함
            val email = binding.EmailLogInIdEditText.text.toString()
            val password = binding.EmailLogInPasswordEditText.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){task ->
                    if(task.isSuccessful) {
                        Toast.makeText(this, "회원가입에 성공했습니다. 로그인 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "회원가입에 실패했습니다. 이미 가입한 이메일일 수 있습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }

    private fun failLogIn() {
        binding.EmailLogInResultTextView.text = "로그인 안됨"
        binding.EmailLogInLogInButton.isEnabled = true
        binding.EmailLogInLogOutButton.isEnabled = false
    }

    private fun successLogIn() {
        binding.EmailLogInResultTextView.text = "로그인 됨"
        binding.EmailLogInLogInButton.isEnabled = false
        binding.EmailLogInLogOutButton.isEnabled = true
    }
}