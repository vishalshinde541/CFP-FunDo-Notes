package com.example.FunDoNotes.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.FunDoNotes.MainActivity
import com.example.loginandregistrationwithfragment.R


class SplashActivity : AppCompatActivity() {

    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handler = Handler()
        handler.postDelayed({


            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, (3000))

    }
}

private fun Handler.postDelayed(function: () -> Unit, delay: Int) {

}
