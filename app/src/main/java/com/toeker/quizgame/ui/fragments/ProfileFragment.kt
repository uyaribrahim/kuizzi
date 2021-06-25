package com.toeker.quizgame.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.toeker.quizgame.R
import com.toeker.quizgame.data.models.User
import com.toeker.quizgame.ui.auth.LoginActivity
import com.toeker.quizgame.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.fragment_profile.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var email: String


    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    private val database by lazy{
        FirebaseFirestore.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        var reference: DocumentReference
        var currentUser: FirebaseUser = auth.currentUser!!
        var uid: String = currentUser.uid

        reference = database.collection("users").document(uid)


        reference.get()
            .addOnCompleteListener(OnCompleteListener {
                if(it.getResult()?.exists() == true){

                    var name: String? = it.getResult()!!.getString("userName")
                    email = it.getResult()!!.getString("userEmail").toString()
                    var createdQuiz: Long? = it.getResult()!!.getLong("createdQuiz")
                    var solvedQuiz: Long? = it.getResult()!!.getLong("solvedQuiz")

                    userName.text = name
                    userEmail.text = email
                    textView3.text = createdQuiz.toString()
                    textView5.text = solvedQuiz.toString()

                }else{

                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient= context?.let { GoogleSignIn.getClient(it,gso) }!!

        logout.setOnClickListener(View.OnClickListener {
            auth.signOut()
            mGoogleSignInClient.signOut().addOnCompleteListener{
            }
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        })
        resetPasswordTxt.setOnClickListener(View.OnClickListener {
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                                    Toast.makeText(context,
                                    "Şifre sıfırlama linki e-posta adresinize gönderildi.",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        })
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
}