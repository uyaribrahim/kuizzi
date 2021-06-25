package com.toeker.quizgame.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.toeker.quizgame.R
import com.toeker.quizgame.ui.QuizViewModel
import com.toeker.quizgame.utils.OnDataAdded
import kotlinx.android.synthetic.main.activity_home.*


@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity(), OnDataAdded {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var quizViewModel: QuizViewModel = ViewModelProviders.of(this).get(QuizViewModel::class.java)
        quizViewModel.init(this)

        val navController = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        //val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.searchFragment, R.id.createQuizFragment,R.id.profileFragment))
        // setupActionBarWithNavController(navController, appBarConfiguration

        bottomNavView.setupWithNavController(navController.navController)

        /*logout.setOnClickListener(View.OnClickListener {

            mGoogleSignInClient.signOut().addOnCompleteListener{
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        })*/
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
    override fun added() {
    }

}