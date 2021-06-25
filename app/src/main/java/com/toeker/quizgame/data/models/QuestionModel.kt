package com.toeker.quizgame.data.models

import java.io.Serializable

class QuestionModel(var question: String? = null, var opt_1: String? = null, var opt_2: String? = null, var opt_3: String?= null,
                    var opt_4: String? = null, var answer: String? = null) : Serializable {
}