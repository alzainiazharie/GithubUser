package com.azharie.alzaini.githubuser3

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.azharie.alzaini.githubuser3.activity.MainActivity

class FirstscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        Handler(mainLooper).postDelayed({
            val intentMain = Intent(this@FirstscreenActivity, MainActivity::class.java)
            startActivity(intentMain)
            finish()
        }, 2000)
    }
}