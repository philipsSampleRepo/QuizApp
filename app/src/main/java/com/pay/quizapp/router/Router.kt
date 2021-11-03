package com.pay.quizapp.router

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pay.quizapp.ui.MainActivity
import com.pay.quizapp.ui.QuizAppQuestions
import com.pay.quizapp.ui.ResultsActivity

object Router {

    fun routeToQuizActivity(context: Context, user:String) {
        val route = Intent(context, QuizAppQuestions::class.java)
        route.putExtra(MainActivity.USER, user)
        context.startActivity(route)
    }

    fun routeToResultsActivity(context: Context, correct: Bundle) {
        val route = Intent(context, ResultsActivity::class.java)
        route.putExtras(correct)
        context.startActivity(route)
    }
}