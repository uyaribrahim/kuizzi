package com.toeker.quizgame.ui.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.toeker.quizgame.R
import com.toeker.quizgame.data.models.QuizModel2
import com.toeker.quizgame.ui.home.QuizActivity
import kotlinx.android.synthetic.main.card_item.view.*


class QuizAdapter(
    private val context: Context,
    private val quizModelArrayList: ArrayList<QuizModel2>

) : PagerAdapter(){

    override fun getCount(): Int {
        return quizModelArrayList.size // return list of items
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //inflate layout card_item.xml
        val view = LayoutInflater.from(context).inflate(R.layout.card_item, container, false)

        //get data
        val model = quizModelArrayList[position]
        val title = model.title
        val author = model.author
        val numberOfQue = model.numberOfQue
        val image = model.imageUrl
        val queModel = model.questions


        // set data to ui views
        //view.bannerImageView.setImageResource(image)

        Glide.with(context)
            .load(image)
            .centerCrop()
            .placeholder(R.drawable.good_morning_img_270x480)
            .into(view.bannerImageView)

        view.txtTitle.text = title
        view.txtAuthor.text = author
        view.txtNumOfQuestion.text = numberOfQue.toString() + "Qs"



        // handle card click
        view.setOnClickListener{
            context.startActivity(Intent(this.context,QuizActivity::class.java).putExtra("question", queModel))
            Toast.makeText(context, "$title", Toast.LENGTH_SHORT).show()
        }

        // add view container
        container.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}