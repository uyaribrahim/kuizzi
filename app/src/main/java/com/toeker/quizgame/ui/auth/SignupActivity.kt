package com.toeker.quizgame.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.toeker.quizgame.R
import com.toeker.quizgame.data.models.User
import com.toeker.quizgame.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    lateinit var uid: String
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)



        var username: String
        var email: String
        var pass: String


        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()


        btnRegister.setOnClickListener(View.OnClickListener {

            username = inputUserName.text.toString()
            email = inputEmail.text.toString()
            pass = inputPassword.text.toString()

            var user: User = User(username,email,pass,0,0)

            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(OnCompleteListener {
                    if (it.isSuccessful) {
                        uid = it.getResult()!!.user!!.uid
                        database
                            .collection("users")
                            .document(uid)
                            .set(user).addOnCompleteListener(OnCompleteListener {
                                if(it.isSuccessful){
                                    val intent=Intent(this, HomeActivity::class.java).apply {}
                                    startActivity(intent)
                                    finish()
                                }
                            })
                        Toast.makeText(this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        })



    }
}