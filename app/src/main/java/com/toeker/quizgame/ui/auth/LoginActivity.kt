package com.toeker.quizgame.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.toeker.quizgame.R
import com.toeker.quizgame.data.models.SavedPreference
import com.toeker.quizgame.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    val Req_Code:Int=123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()


        signInButton.setOnClickListener(View.OnClickListener {
            signInGoogle()
        })

        btnLogin.setOnClickListener{

            var email: String
            var pass: String

            email = inputEmail.text.toString()
            pass = inputPassword.text.toString()

            auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(OnCompleteListener {

                if(it.isSuccessful){
                    Toast.makeText(this, "Giriş Başarılı", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java).apply {  }
                    startActivity(intent)
                    finish()

                }else{
                    Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })

        }
        signUpText.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java).apply {  }
            startActivity(intent)
        }
    }

    private fun signInGoogle(){

        val signInIntent:Intent=googleSignInClient.signInIntent
        startActivityForResult(signInIntent,Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Req_Code){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException){
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {
                SavedPreference.setEmail(this,account.email.toString())
                SavedPreference.setUsername(this,account.displayName.toString())
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser =  auth.currentUser

        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        else if(currentUser != null){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

    }
}