package com.pay.quizapp.ui

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.pay.quizapp.R
import com.pay.quizapp.databinding.ActivityQuizAppQuestionsBinding
import com.pay.quizapp.model.Question
import com.pay.quizapp.router.Router
import com.pay.quizapp.ui.MainActivity.Companion.USER
import com.pay.quizapp.utils.QuestionUtilities

class QuizAppQuestions : AppCompatActivity() {

    private val next_str: String = "Next Question"
    private val TAG: String = QuestionUtilities::class.java.simpleName

    private lateinit var quizBinding: ActivityQuizAppQuestionsBinding
    private lateinit var view: View
    private lateinit var tv_progress: TextView
    private lateinit var progress: LinearProgressIndicator

    private lateinit var option_1: Button
    private lateinit var option_2: Button
    private lateinit var option_3: Button
    private lateinit var option_4: Button
    private lateinit var submit: Button

    private lateinit var tv_question: TextView
    private lateinit var img_flag: ImageView

    private var total: Int = 0
    private var current: Int = 0
    private var selected: Int = 0
    private var correct: Int = 0
    private var unAnswered: Int = 0

    private var user: String? = ""

    private var isAnswered: Boolean = false

    private lateinit var questions: ArrayList<Question>
    private lateinit var buttonsList: ArrayList<Button>
    private lateinit var question: Question

    companion object {
        const val SCORE: String = "SCORE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding = ActivityQuizAppQuestionsBinding.inflate(layoutInflater)
        view = quizBinding.root
        setContentView(view)
        questions = QuestionUtilities.getQuestions()
        Log.i(TAG, "Questions list size: ${questions.size}")
        initUI()
        addUiComponents()
        submitAction()
        bindData(current)
        handleOptions()
        getUser(intent)
    }

    fun getUser(intent: Intent) {
        if (intent != null) {
            intent.getStringExtra(MainActivity.USER)?.let {
                user = intent.getStringExtra(MainActivity.USER).toString()
                Log.i(TAG, "getUser: $user")
            }
        }
    }

    fun initUI() {
        option_1 = quizBinding.btnOption1
        option_2 = quizBinding.btnOption2
        option_3 = quizBinding.btnOption3
        option_4 = quizBinding.btnOption4
        submit = quizBinding.btnSubmit

        tv_question = quizBinding.tvQuestion
        tv_progress = quizBinding.tvProgress

        img_flag = quizBinding.imageView
        progress = quizBinding.progressBar
        progress.max = 10
        total = questions.size
    }

    fun bindData(progressVal: Int) {

        if (progressVal >= questions.size) {
            return
        }
        reset()
        question = questions.get(progressVal)

        tv_question.text = question.question
        img_flag.setImageResource(question.image)

        option_1.text = question.optionOne
        option_2.text = question.optionTwo
        option_3.text = question.optionThree
        option_4.text = question.optionFour

        val strBuilder: StringBuilder = StringBuilder()
        strBuilder.append(progressVal + 1)
        strBuilder.append("/")
        strBuilder.append(total)
        tv_progress.text = strBuilder.toString()

        progress.setProgress(progressVal + 1)
    }

    fun submitAction() {
        submit.setOnClickListener(View.OnClickListener {
            Log.i(TAG, "submitAction: ")
            Log.i(TAG, "stats: $correct  $unAnswered")

            if (!isAnswered) {
                selected = 0
            }

            if (submit.text == "Submit") {
                if (!isAnswered) {
                    ++unAnswered
                }
                lockOrUnlockAnswers(false)
                highlightAnswers(question.correctAnswer, selected)
            } else if (submit.text == next_str) {
                lockOrUnlockAnswers(true)
                bindData(++current)
            } else {
                var bundle: Bundle = Bundle()
                bundle.putString(USER, user)
                bundle.putInt(SCORE, correct)
                Router.routeToResultsActivity(this, bundle)
                finish()
            }
            isAnswered = false
        })
    }

    fun changeSubmitBtnText() {
        if (current + 1 == questions.size) {
            submit.text = "Finish"
            return
        }
        submit.setText(next_str)
    }

    fun revertSubmitBtnText() {
        submit.setText("Submit")
    }

    fun handleOptions() {
        option_1.setOnClickListener {
            Log.i(TAG, "handleOptions: 1")
            selected = 1
            highlightSelected(1)
            isAnswered = true
        }
        option_2.setOnClickListener {
            selected = 2
            highlightSelected(2)
            isAnswered = true
        }
        option_3.setOnClickListener {
            selected = 3
            highlightSelected(3)
            isAnswered = true
        }
        option_4.setOnClickListener {
            selected = 4
            highlightSelected(4)
            isAnswered = true
        }


    }

    fun highlightSelected(index: Int) {
        for (i in 0 until buttonsList.size) {
            var btn: Button = buttonsList.get(i)
            if (i == index - 1) {
                btn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
                btn.setTypeface(Typeface.DEFAULT_BOLD)
                btn.setTextColor(ContextCompat.getColor(this, R.color.black))
            } else {
                btn.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500))
                btn.setTypeface(Typeface.DEFAULT)
                btn.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
        }
    }

    fun highlightAnswers(answer: Int, selected: Int) {
        changeSubmitBtnText()
        if (answer == selected) {
            ++correct
        }
        for (i in 0 until buttonsList.size) {
            var btn: Button = buttonsList.get(i)
            if (i + 1 == answer) {
                btn.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
                btn.setTypeface(Typeface.DEFAULT_BOLD)
                btn.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else if (selected == i + 1) {
                btn.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                btn.setTypeface(Typeface.DEFAULT)
            }
        }
    }

    fun addUiComponents() {
        buttonsList = ArrayList<Button>()
        buttonsList.add(option_1)
        buttonsList.add(option_2)
        buttonsList.add(option_3)
        buttonsList.add(option_4)
    }

    fun lockOrUnlockAnswers(state: Boolean) {
        for (i in 0 until buttonsList.size) {
            var btn: Button = buttonsList[i]
            btn.isEnabled = state
        }
    }

    fun reset() {
        revertSubmitBtnText()
        for (i in 0 until buttonsList.size) {
            var btn: Button = buttonsList[i]
            btn.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_500))
            btn.setTypeface(Typeface.DEFAULT)
            btn.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
    }
}