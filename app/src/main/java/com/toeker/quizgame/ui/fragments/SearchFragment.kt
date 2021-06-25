package com.toeker.quizgame.ui.fragments

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.toeker.quizgame.R
import com.toeker.quizgame.data.models.QuizModel2
import com.toeker.quizgame.data.repositories.Repo
import com.toeker.quizgame.ui.QuizViewModel
import com.toeker.quizgame.ui.adapters.QuizRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    private lateinit var quizAdapter: QuizRecyclerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var quizViewModel: QuizViewModel
    private var arrayList: ArrayList<QuizModel2> = ArrayList()
    var pin: String  = ""

    private val database by lazy{
        FirebaseFirestore.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager = LinearLayoutManager(context)
        rvSearch.layoutManager = linearLayoutManager

        quizViewModel = ViewModelProviders.of(requireActivity()).get(QuizViewModel::class.java)


        searchEditText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN &&
                        keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    searchQuiz(searchEditText.text.toString())
                    searchEditText.isCursorVisible = false
                    return true
                }
                return false
            }
        })
        pinEditText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                        pin = pinEditText.text.toString()
                        loadQuizData()
                    //pinEditText.isCursorVisible = false
                    return true
                }
                return false
            }
        })
    }

    private fun loadQuizData() {
        database.collection("quiz").whereEqualTo("pin", pin.toInt())
            .get().addOnSuccessListener {

            if(!it.isEmpty){

                var list: List<DocumentSnapshot> = it.documents

                arrayList.clear()

                for(documentSnapshot: DocumentSnapshot in list){

                    arrayList.add(documentSnapshot.toObject(QuizModel2::class.java)!!)
                }
                loadCards()
            }
        }.addOnFailureListener{ exception ->

            Log.e(TAG,"onFailure",exception)
        }
    }
    private fun loadCards() {

    quizAdapter = arrayList?.let {
            context?.let { it1 ->
                QuizRecyclerAdapter(
                    it1, it)
            }
        }!!

        //set adapter to recycleview
        rvSearch.setHasFixedSize(true)
        rvSearch.adapter = this.quizAdapter
    }
    private fun searchQuiz(aramaKelime: String){

        var arrayList2: ArrayList<QuizModel2> = ArrayList()
        arrayList2 = quizViewModel.getQuiz()?.getValue()!!
        arrayList.clear()
        for(quiz in arrayList2){
            if(quiz.title?.toLowerCase()?.contains(aramaKelime) == true){
                arrayList.add(quiz)
            }
        }
        loadCards()
    }
}