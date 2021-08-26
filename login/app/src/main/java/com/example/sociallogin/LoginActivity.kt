package com.example.sociallogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setKakaoLoginBtn()
        setNaverLoginBtn()
    }

    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("MAIN_SET_CALLBACK", "로그인 실")
        } else if (token != null) {
            Log.d("MAIN_SET_CALLBACK", token.toString())
            startMainActivity()
        }
    }


    private fun setNaverLoginBtn() {
        btn_naver_login.setOnClickListener {

        }
    }


    private fun setKakaoLoginBtn() {
        btn_kakao_login.setOnClickListener {
            UserApiClient.instance.me { user, error ->
                if (error != null) { // 기존 로그인 정보 없는 경 -> 로그인 시도
                    LoginClient.instance.run {
                        if (isKakaoTalkLoginAvailable(this@LoginActivity)) { //카카오톡 앱이 있을 경우
                            loginWithKakaoTalk(this@LoginActivity, callback = callback) // 카카오톡으로 로그인
                        } else {
                            loginWithKakaoAccount(this@LoginActivity, callback = callback) // 카카오톨 계정으로 로그인(브라우저 기반)
                        }
                    }
                } else { //기존 로그인 정보 있을 경우
                    startMainActivity()
                }
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}