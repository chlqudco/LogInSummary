package com.chlqudco.develop.loginsummary

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.chlqudco.develop.loginsummary.databinding.ActivityKakaoLogInBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class KakaoLogInActivity : AppCompatActivity() {
    private val binding by lazy { ActivityKakaoLogInBinding.inflate(layoutInflater) }

    // 카카오계정으로 로그인 공통 callback 구성
    // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
    @SuppressLint("SetTextI18n")
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            binding.KakaoResultTextView.text = "로그인 실패" + error.message.toString()
        } else if (token != null) {
            binding.KakaoResultTextView.text = "로그인 성공"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {

        //일단 로그아웃 시키기
        UserApiClient.instance.logout { error ->
            if (error != null) {
                binding.KakaoResultTextView.text = "로그인 안됨"
            }
            else {
                binding.KakaoResultTextView.text = "로그인 안됨"
            }
        }



        //로그인 버튼
        binding.KakaoLogInButton.setOnClickListener {
            logIn()
        }

        //로그아웃 버튼
        binding.KakaoLogOutButton.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    binding.KakaoResultTextView.text = "로그아웃 실패"
                }
                else {
                    binding.KakaoResultTextView.text = "로그인 안됨"
                }
            }
        }
    }

    private fun logIn() {
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    binding.KakaoResultTextView.text = "로그인 안됨"

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    binding.KakaoResultTextView.text = "로그인 성공"
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }


    }
}