package com.example.sociallogin

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {

    private var firebaseAnalytics: FirebaseAnalytics? = null

    private val bundle = Bundle()
    private val buttons = ArrayList<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setButtons()
        setWebView()
        setFirebaseAnalytics()
    }

    private fun setButtons() {
        buttons.add(btn_1)
        buttons.add(btn_2)
        buttons.add(btn_3)
        btn_1.setOnClickListener { addDataToFirebaseAnalytics(btn_1.toString()) }
        btn_2.setOnClickListener { addDataToFirebaseAnalytics(btn_2.toString()) }
        btn_3.setOnClickListener { addDataToFirebaseAnalytics(btn_3.toString()) }
    }

    private fun setFirebaseAnalytics() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    private fun setWebView() {
        wb_temp.loadUrl("https://www.google.com")
    }

    private fun addDataToFirebaseAnalytics(btnName: String) {
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "버튼 : $btnName")
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")

        this.firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        Toast.makeText(this, " $bundle", Toast.LENGTH_SHORT).show()
    }
}
