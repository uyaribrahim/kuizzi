package com.toeker.quizgame.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.toeker.quizgame.R
import com.toeker.quizgame.data.models.QuizModel2
import com.toeker.quizgame.ui.home.QuizActivity
import kotlinx.android.synthetic.main.card_item.view.*

class QuizRecyclerAdapter(
    private val context: Context,
    private val quizModelArrayList: ArrayList<QuizModel2>
) : RecyclerView.Adapter<QuizRecyclerAdapter.QuizViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuizRecyclerAdapter.QuizViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_item,parent,false)
        return QuizViewHolder(v)
    }

    override fun onBindViewHolder(holder: QuizRecyclerAdapter.QuizViewHolder, position: Int) {

        val model = quizModelArrayList[position]

        val title = model.title
        val author = model.author
        val numberOfQue = model.numberOfQue
        val image = model.imageUrl
        val queModel = model.questions


        // set data to ui views
        //holder.itemView.bannerImageView.setImageResource(image)
        Glide.with(context)
            .load(image)
            .centerCrop()
            .placeholder(R.drawable.good_morning_img_270x480)
            .into(holder.itemView.bannerImageView)

        holder.itemView.txtTitle.text = title
        holder.itemView.txtAuthor.text = author
        holder.itemView.txtNumOfQuestion.text = numberOfQue.toString() + "Qs"

        holder.itemView.setOnClickListener(View.OnClickListener {
            context.startActivity(Intent(this.context, QuizActivity::class.java).putExtra("question", queModel))
        })
    }

    override fun getItemCount(): Int {
        return quizModelArrayList.size

    }

    class QuizViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}
