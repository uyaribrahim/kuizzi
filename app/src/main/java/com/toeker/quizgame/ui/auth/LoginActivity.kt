package com.toeker.quizgame.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.toeker.quizgame.R
import com.toeker.quizgame.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        btnLogin.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java).apply {  }
            startActivity(intent)

        }
        signUpText.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java).apply {  }
            startActivity(intent)
        }



    }
}