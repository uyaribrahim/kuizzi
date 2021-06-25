package com.toeker.quizgame.ui


import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.toeker.quizgame.data.models.QuizModel2
import com.toeker.quizgame.data.repositories.Repo
import com.toeker.quizgame.ui.fragments.HomeFragment


class QuizViewModel : ViewModel() {


    var liveData: MutableLiveData<ArrayList<QuizModel2>>? = null

    fun init(context: Context){

        if(liveData != null){
            return
        }else{

            liveData = Repo.getInstance(context)?.getQuizData()
        }

    }
    fun realTime(context: Context){
        liveData = null
        liveData = Repo.getInstance(context)?.getQuizData()
    }
    fun getQuiz(): LiveData<ArrayList<QuizModel2>>?{
        return liveData
    }



    /*database.collection("quiz").get().addOnSuccessListener { documents ->
        for(document in documents){
            quizModelList.add(QuizModel(
                    "${document.data["title"]}",
                    "${document.data["author"]}",
                    "${document.data["numberOfQue"]}".toInt(),
                    R.drawable.good_morning_img_270x480,
                    questionModelList1
            ))
            Log.v(TAG, "Check1 " + quizModelList.size);
        }
        sinavlar.postValue(quizModelList)
    }*/
    }
