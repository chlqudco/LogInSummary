package com.chlqudco.develop.loginsummary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chlqudco.develop.loginsummary.databinding.ActivityNaverLogInBinding
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class NaverLogInActivity : AppCompatActivity() {

    private val binding by lazy { ActivityNaverLogInBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        NaverIdLoginSDK.initialize(this, "gCRYXhmVbGVAVWTBGPZu", "yrokdxv8Uh", "LogInSummary")

        //로그아웃 하고 시작
        NaverIdLoginSDK.logout()

        //로그인 버튼
        binding.NaverLogInButton.setOnClickListener {
            logIn()
        }

        //로그아웃 버튼
        binding.NaverLogOutButton.setOnClickListener {
            logOut()
        }
    }

    private fun logIn() {
        var token: String? =""

        val profileCallback = object : NidProfileCallback<NidProfileResponse>{
            override fun onSuccess(result: NidProfileResponse) { binding.NaverResultTextView.text = "로그인 성공" }
            override fun onFailure(httpStatus: Int, message: String) { binding.NaverResultTextView.text = "로그인 실패" }
            override fun onError(errorCode: Int, message: String) { binding.NaverResultTextView.text = "로그인 오류" }
        }

        val oauthLoginCallback = object : OAuthLoginCallback{
            override fun onSuccess() {
                token = NaverIdLoginSDK.getAccessToken()
                NidOAuthLogin().callProfileApi(profileCallback)
            }
            override fun onFailure(httpStatus: Int, message: String) { binding.NaverResultTextView.text = "로그인 실패" }
            override fun onError(errorCode: Int, message: String) { binding.NaverResultTextView.text = "로그인 오류" }
        }

        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
    }

    private fun logOut() {
        NaverIdLoginSDK.logout()
        binding.NaverResultTextView.text = "로그인 안함"
    }
}