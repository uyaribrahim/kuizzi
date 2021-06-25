package com.toeker.quizgame.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.toeker.quizgame.R
import com.toeker.quizgame.data.models.QuizModel2
import com.toeker.quizgame.ui.QuizViewModel
import com.toeker.quizgame.ui.adapters.QuizAdapter
import com.toeker.quizgame.utils.OnDataAdded
import kotlinx.android.synthetic.main.fragment_home.*


@Suppress("DEPRECATION")
class HomeFragment : Fragment(), OnDataAdded  {

    private lateinit var quizAdapter: QuizAdapter
    private var arrayList: ArrayList<QuizModel2>? = ArrayList()
    private lateinit var quizViewModel: QuizViewModel

    private val reference = Firebase.firestore.collection("quiz")

    var progressStatus = 0;
    lateinit var progressBar: ProgressBar
    val handler: Handler = Handler()

    private val database by lazy{
        FirebaseFirestore.getInstance()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progressBar)

        getData()
        realTimeUpdates()

        }
    fun getData(){
        quizViewModel = ViewModelProviders.of(requireActivity()).get(QuizViewModel::class.java)

        if(quizViewModel.getQuiz()?.getValue()?.size == 0){
            Thread(Runnable {

                while (quizViewModel.getQuiz()?.getValue()?.size == 0) {
                    progressStatus += 1

                    try {
                        Thread.sleep(200)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                    handler.post(Runnable {
                        progressBar.progress = progressStatus
                        if(quizViewModel.getQuiz()?.getValue()?.size != 0){

                            Log.e("TAG", "ValueList : " + arrayList?.size)

                            setViewPager()

                            progressBar.visibility = View.INVISIBLE

                            //Log.e("TAG", "Fragment")
                        }
                    })
                }
            }).start()
        }else{
            Log.e("########", quizViewModel.getQuiz()?.getValue()?.size.toString())
            setViewPager()
            progressBar.visibility = View.INVISIBLE
        }
    }
    fun realTimeUpdates(){

        reference.addSnapshotListener { querySnapshot, exception ->

            exception?.let {
                Toast.makeText(context,it.message,Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            querySnapshot?.let {
                    quizViewModel.realTime(requireActivity())
            }
        }
    }
    fun setViewPager(){
        quizAdapter = quizViewModel?.getQuiz()?.getValue()?.let {
            context?.let { it1 ->
                QuizAdapter(
                    it1, it)
            }
        }!!
        viewPager.adapter = quizAdapter
        viewPager2.adapter = quizAdapter

        viewPager2.setPadding(120,0,120,0)
        viewPager.setPadding(120, 0, 120, 0)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)


        return view
    }

    override fun added() {
        quizAdapter.notifyDataSetChanged()
    }
}