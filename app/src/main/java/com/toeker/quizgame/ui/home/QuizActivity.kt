package com.toeker.quizgame.ui.home

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.toeker.quizgame.R
import com.toeker.quizgame.data.models.QuestionModel
import com.toeker.quizgame.ui.auth.SignupActivity
import io.grpc.internal.SharedResourceHolder
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity() {

    lateinit var questionModel: QuestionModel
    var id = 0;
    lateinit var question: ArrayList<QuestionModel>
    var dogru: Int = 0
    var yanlis: Int = 0

    private val database by lazy{
        FirebaseFirestore.getInstance()
    }
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    var currentUser = auth.currentUser
    var uid: String = currentUser!!.uid
    var reference: DocumentReference = database.collection("users").document(uid)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        question = intent.getSerializableExtra("question") as ArrayList<QuestionModel>

        questionModel = question[id]

        textViewSoru.text = questionModel.question
        buttonA.setText(questionModel.opt_1)
        buttonB.setText(questionModel.opt_2)
        buttonC.setText(questionModel.opt_3)
        buttonD.setText(questionModel.opt_4)

        btnNext.setOnClickListener(View.OnClickListener {
            if(id < question.size - 1){
                nextQuestion()
            }else{
                reference.update("solvedQuiz", FieldValue.increment(1))
                showDialog()
            }
        })

        buttonA.setOnClickListener(View.OnClickListener {
            answerController(buttonA.text.toString(), buttonA )
        })
        buttonB.setOnClickListener(View.OnClickListener {
            answerController(buttonB.text.toString(), buttonB )
        })
        buttonC.setOnClickListener(View.OnClickListener {
            answerController(buttonC.text.toString(), buttonC )
        })
        buttonD.setOnClickListener(View.OnClickListener {
            answerController(buttonD.text.toString(), buttonD )
        })

    }

    fun answerController(buttonText: String, btn: Button){

        btn.setBackgroundColor(Color.GREEN);
        if(btn.text == questionModel.answer){
            dogru++
        }
        else{
            yanlis++
        }
        buttonA.isEnabled = false
        buttonB.isEnabled = false
        buttonC.isEnabled = false
        buttonD.isEnabled = false

    }
    fun resetButtonColor(){
        buttonA.setBackgroundColor(ContextCompat.getColor(applicationContext , R.color.gray))
        buttonB.setBackgroundColor(ContextCompat.getColor(applicationContext , R.color.gray))
        buttonC.setBackgroundColor(ContextCompat.getColor(applicationContext , R.color.gray))
        buttonD.setBackgroundColor(ContextCompat.getColor(applicationContext , R.color.gray))
    }

    fun nextQuestion(){
        id += 1
        questionModel = question[id]
        resetButtonColor()
        textViewSoru.text = questionModel.question
        buttonA.setText(questionModel.opt_1)
        buttonB.setText(questionModel.opt_2)
        buttonC.setText(questionModel.opt_3)
        buttonD.setText(questionModel.opt_4)

        buttonA.isEnabled = true
        buttonB.isEnabled = true
        buttonC.isEnabled = true
        buttonD.isEnabled = true
    }

    fun showDialog() {
        val dialog = this?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (dialog?.getWindow() != null) {
            val colorDrawable = ColorDrawable(Color.TRANSPARENT)
            dialog.getWindow()!!.setBackgroundDrawable(colorDrawable)
        }
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.quiz_end_activity)
        dialog?.show()



        var homeButton: FloatingActionButton = dialog.findViewById(R.id.homeButton)
        var dogruText: TextView = dialog.findViewById(R.id.txtDogru)
        var yanlisText: TextView = dialog.findViewById(R.id.txtYanlis)

        dogruText.setText("Doğru: $dogru")
        yanlisText.setText("Yanlış: $yanlis")

        homeButton.setOnClickListener(View.OnClickListener {
            dialog?.dismiss()
            onBackPressed()

        })


    }

}