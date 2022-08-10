package com.chlqudco.develop.loginsummary

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import com.chlqudco.develop.loginsummary.databinding.ActivityGitHubLogInBinding
import com.chlqudco.develop.loginsummary.retrofit.RetrofitUtil
import com.google.firebase.auth.GithubAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GitHubLogInActivity : AppCompatActivity() {

    private val binding by lazy { ActivityGitHubLogInBinding.inflate(layoutInflater) }

    private val auth by lazy { Firebase.auth }

    private val provider = OAuthProvider.newBuilder("github.com")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {

        //로그아웃 시키기
        if (auth.currentUser != null){
            auth.signOut()
        }
        binding.GitHubLogInButton.isEnabled = true
        binding.GitHubLogOutButton.isEnabled = false

        //로그인 버튼
        binding.GitHubLogInButton.setOnClickListener {
            provider.addCustomParameter("login", "chlqudco84@gmail.com")

            auth.startActivityForSignInWithProvider(this, provider.build())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        binding.GitHubResultTextView.text = "로그인 성공"
                        binding.GitHubLogInButton.isEnabled = false
                        binding.GitHubLogOutButton.isEnabled = true
                    } else{
                        binding.GitHubResultTextView.text = "로그인 실패"
                        binding.GitHubLogInButton.isEnabled = true
                        binding.GitHubLogOutButton.isEnabled = false
                        Log.e("asd", task.exception.toString())
                    }
                }
        }

        binding.GitHubLogOutButton.setOnClickListener {
            auth.signOut()
            binding.GitHubLogInButton.isEnabled = true
            binding.GitHubLogOutButton.isEnabled = false
        }

    }

}