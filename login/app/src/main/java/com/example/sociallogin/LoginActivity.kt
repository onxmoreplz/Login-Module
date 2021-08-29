package com.example.sociallogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var nhnOAuthLoginModule: OAuthLogin
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        context = this

        setKakaoLoginBtn()
        setNaverLoginBtn()
    }

    //카카오 로그인 콜백리스너
    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("MAIN_SET_CALLBACK", "로그인 실")
        } else if (token != null) {
            Log.d("MAIN_SET_CALLBACK", token.toString())
            startMainActivity()
        }
    }

    private fun setNaverLoginBtn() {
        nhnOAuthLoginModule = OAuthLogin.getInstance() //로그인 인스턴스 초기화
        nhnOAuthLoginModule.init(
            this,
            R.string.naver_client_id.toString(),
            R.string.naver_client_secret.toString(),
            R.string.naver_client_name.toString()
        )
        btn_naver_login.setOAuthLoginHandler(oAuthLoginHandler)
    }

    // 네이버 로그인 핸들
    private val oAuthLoginHandler: OAuthLoginHandler = object : OAuthLoginHandler() {
        override fun run(success: Boolean) {
            if (success) {
                val accessToken: String = nhnOAuthLoginModule.getAccessToken(baseContext)
                val refreshToken: String = nhnOAuthLoginModule.getRefreshToken(baseContext)
                val expiresAt: Long = nhnOAuthLoginModule.getExpiresAt(baseContext)
            } else {
                val errorCode: String = nhnOAuthLoginModule.getLastErrorCode(context).code
                val errorDesc = nhnOAuthLoginModule.getLastErrorDesc(context)

                Toast.makeText(
                    baseContext, "errorCode : " + errorCode
                            + "errorDescription : " + errorDesc, Toast.LENGTH_SHORT
                ).show()
            }
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

    fun successNaverLogin() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    companion object {
        private val TAG = LoginActivity::class.java!!.simpleName
    }
}