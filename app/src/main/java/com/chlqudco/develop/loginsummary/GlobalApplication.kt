package com.chlqudco.develop.loginsummary

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "4709f294ca044f8f524e0b6b8e29e7ca")
    }
}