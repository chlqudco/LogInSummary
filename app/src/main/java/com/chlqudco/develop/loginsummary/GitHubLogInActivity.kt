package com.chlqudco.develop.loginsummary

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import com.chlqudco.develop.loginsummary.databinding.ActivityGitHubLogInBinding
import com.chlqudco.develop.loginsummary.retrofit.RetrofitUtil
import com.google.firebase.auth.GithubAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GitHubLogInActivity : AppCompatActivity() {

    private val binding by lazy { ActivityGitHubLogInBinding.inflate(layoutInflater) }

    private val auth by lazy { Firebase.auth }
    private var authToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {

        getAccessToken()

        binding.progressBar.isVisible = false
        if (auth.currentUser != null) {
            auth.signOut()
        }

        //로그인 버튼 연동
        binding.loginButton.setOnClickListener {
            binding.progressBar.isVisible = true
            logIn()
        }

    }

    private fun logIn() {
        val loginUri = Uri.Builder().scheme("https").authority("github.com")
            .appendPath("login")
            .appendPath("oauth")
            .appendPath("authorize")
            .appendQueryParameter("client_id", "d237e8f4f42d8e6b0105")
            .build()

        CustomTabsIntent.Builder().build().also {
            it.launchUrl(this, loginUri)
        }


        val credential = GithubAuthProvider.getCredential(authToken!!)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    binding.progressBar.isVisible = false
                    binding.progressTextView.text = "로그인 성공"
                }
            }
    }

    private fun getAccessToken() {
        CoroutineScope(Dispatchers.Main).launch {
            val response = RetrofitUtil.authApiService.getAccessToken(
                "d237e8f4f42d8e6b0105",
                "c110c1a8ed986799cf1370216bd4b4625838f348"
            )
            authToken = response.accessToken
        }

    }
}