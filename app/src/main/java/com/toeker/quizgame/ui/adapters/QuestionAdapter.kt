package com.toeker.quizgame.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.toeker.quizgame.R
import com.toeker.quizgame.data.models.QuestionModel
import kotlinx.android.synthetic.main.card_question.view.*

class QuestionAdapter(

    private val context: Context,
    private val questionModelArrayList: ArrayList<QuestionModel>) : RecyclerView.Adapter<QuestionAdapter.QueViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionAdapter.QueViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_question,parent,false)
        return QuestionAdapter.QueViewHolder(v)
    }

    override fun onBindViewHolder(holder: QuestionAdapter.QueViewHolder, position: Int) {

        val model = questionModelArrayList[position]

        val title = model.question

        holder.itemView.txtQue.text = title

    }

    override fun getItemCount(): Int {
        return questionModelArrayList.size
    }

    class QueViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


}