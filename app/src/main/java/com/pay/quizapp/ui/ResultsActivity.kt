package com.pay.quizapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.pay.quizapp.R
import com.pay.quizapp.databinding.ActivityResultsBinding
import com.pay.quizapp.utils.QuestionUtilities

class ResultsActivity : AppCompatActivity() {
    private val TAG: String = ResultsActivity::class.java.simpleName
    private var score: Int = 0
    private var user: String? = ""

    private lateinit var resultsBinding: ActivityResultsBinding
    private lateinit var view: View
    private lateinit var bundle: Bundle

    private lateinit var tv_score: TextView
    private lateinit var tv_user: TextView

    private lateinit var finish: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        resultsBinding = ActivityResultsBinding.inflate(layoutInflater)
        view = resultsBinding.root
        setContentView(view)

        if (intent != null) {
            intent.extras?.let {
                bundle = it
                user = bundle.getString(MainActivity.USER)
                score = bundle.getInt(QuizAppQuestions.SCORE)
            }

            Log.i(
                TAG,
                "score: $user   $score"
            )
        }

        initUi()
    }

    fun initUi() {
        tv_score = resultsBinding.tvScore
        tv_user = resultsBinding.tvUser
        finish = resultsBinding.btnFinish

        finish.setOnClickListener(View.OnClickListener {
            Log.i(TAG, "initUi: Finished")
            finish()
        })

        tv_score.text =
            "Your score is " + score.toString() + " " + "out of ${QuestionUtilities.getQuestions().size}"
        tv_user.text = user
    }

}