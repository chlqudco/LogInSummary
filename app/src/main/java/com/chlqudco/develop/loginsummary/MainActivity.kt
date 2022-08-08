package com.chlqudco.develop.loginsummary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //다크모드 금지
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun buttonClicked(v: View) {

        val intent: Intent = when(v.id){
            R.id.appleLogInButton ->  Intent(this, AppleLogInActivity::class.java)
            R.id.emailLogInButton ->  Intent(this, EmailLogInActivity::class.java)
            R.id.faceBookLogInButton ->  Intent(this, FaceBookLogInActivity::class.java)
            R.id.gitHubLogInButton ->  Intent(this, GitHubLogInActivity::class.java)
            R.id.googleLogInButton ->  Intent(this, GoogleLogInActivity::class.java)
            R.id.guestLogInButton -> Intent(this, GuestLogInActivity::class.java)
            R.id.kakaoLogInButton ->  Intent(this, KakaoLogInActivity::class.java)
            R.id.microsoftLogInButton ->  Intent(this, MicrosoftLogInActivity::class.java)
            R.id.naverLogInButton ->  Intent(this, NaverLogInActivity::class.java)
            R.id.phoneLogInButton ->  Intent(this, PhoneLogInActivity::class.java)
            else ->  Intent(this, AppleLogInActivity::class.java)
        }
        startActivity(intent)
    }
}