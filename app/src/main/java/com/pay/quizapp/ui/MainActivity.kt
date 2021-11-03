package com.pay.quizapp.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pay.quizapp.databinding.ActivityMainBinding
import com.pay.quizapp.router.Router

class MainActivity : AppCompatActivity() {
    private val TAG: String = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding
    private lateinit var view: View

    companion object {
        const val USER: String = "USER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)
        btnAction()
    }

    private fun btnAction() {
        binding.btnStart.setOnClickListener(View.OnClickListener {
            Log.i(TAG, "btnAction: ")
            if (!TextUtils.isEmpty(binding.etName.text)) {
                Router.routeToQuizActivity(this, binding.etName.text.toString())
            } else {
                Toast.makeText(
                    this, "Name cannot be empty...!", Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}