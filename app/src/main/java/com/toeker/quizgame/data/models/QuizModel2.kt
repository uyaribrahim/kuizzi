package com.toeker.quizgame.data.models

import java.io.Serializable

data class QuizModel2(var userID: String? = null, var author: String? = null, var title: String? = null,
                      var numberOfQue: Int? = null, var description: String? = null,
                      var visible: Boolean? = null, var pin: Int? = null, var imageUrl: String? = null,
                      var questions: ArrayList<QuestionModel>? = null)  {

}