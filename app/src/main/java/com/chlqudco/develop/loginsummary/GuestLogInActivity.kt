package com.chlqudco.develop.loginsummary

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.chlqudco.develop.loginsummary.databinding.ActivityGuestLogInBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class GuestLogInActivity : AppCompatActivity() {

    private val binding by lazy { ActivityGuestLogInBinding.inflate(layoutInflater) }
    private val auth by lazy { Firebase.auth }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        if (auth.currentUser != null){ auth.signOut() }
        initLogIn()

        //로그인 처리
        binding.GuestLogInButton.setOnClickListener {
            logIn()
        }

        //로그아웃 처리
        binding.GuestLogOutButton.setOnClickListener {
            auth.signOut()
            initLogIn()
        }
    }

    private fun logIn() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    successLogIn()
                } else {
                    initLogIn()
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun successLogIn() {
        binding.GuestLogInButton.isEnabled = false
        binding.GuestLogOutButton.isEnabled = true
        binding.GuestResultTextView.text = "로그인 성공"
    }

    private fun initLogIn() {
        binding.GuestLogInButton.isEnabled = true
        binding.GuestLogOutButton.isEnabled = false
        binding.GuestResultTextView.text = "로그인 안됨"
    }
}