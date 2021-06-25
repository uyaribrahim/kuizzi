package com.toeker.quizgame.data.repositories

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.toeker.quizgame.data.models.QuizModel2
import com.toeker.quizgame.utils.OnDataAdded
import kotlin.reflect.typeOf

class Repo {

    //private lateinit var instance: Repo
    private var arrayList: ArrayList<QuizModel2> = ArrayList()
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var TAG: String = "Repo"

    companion object{
         var instance: Repo? = null
         var onDataAdded: OnDataAdded? = null

        fun getInstance(context: Context): Repo?{

            if(instance == null){
                instance = Repo()
            }
            onDataAdded =  context as OnDataAdded
            return instance
        }
    }

    fun getQuizData(): MutableLiveData<ArrayList<QuizModel2>>{

        loadQuizData()

        var data: MutableLiveData<ArrayList<QuizModel2>> = MutableLiveData()
        data.setValue(arrayList)
        return data
    }

    private fun loadQuizData() {
        db.collection("quiz").whereEqualTo("visible", true).get().addOnSuccessListener {
            if(!it.isEmpty){
                var list: List<DocumentSnapshot> = it.documents
                arrayList.clear()
                for(documentSnapshot: DocumentSnapshot in list){
                    arrayList.add(documentSnapshot.toObject(QuizModel2::class.java)!!)
                }
                //Log.e(TAG, "onSucces: added")
                onDataAdded?.added()
            }
        }.addOnFailureListener{ exception ->
            //Log.e(TAG,"onFailure",exception)
        }
    }


}