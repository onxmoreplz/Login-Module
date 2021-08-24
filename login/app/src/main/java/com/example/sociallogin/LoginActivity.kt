package com.example.sociallogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setCallbackFromSocialLogin()
    }


    private fun setCallbackFromSocialLogin() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("MAIN_SET_CALLBACK", error.message.toString())
            } else if (token != null) {
                Log.d("MAIN_SET_CALLBACK", token.toString())
                startMainActivity()
            }
        }

        btn_kakao_login.setOnClickListener {
            Toast.makeText(this, "cpal", Toast.LENGTH_SHORT).show()
            LoginClient.instance.run {
                if (isKakaoTalkLoginAvailable(this@LoginActivity)) {
                    loginWithKakaoTalk(this@LoginActivity, callback = callback) // 카카오톡으로 로그
                } else {
                    loginWithKakaoAccount(this@LoginActivity, callback = callback) // 카카오톨 계정으로 로그인(브라우저 기반)
                }
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}