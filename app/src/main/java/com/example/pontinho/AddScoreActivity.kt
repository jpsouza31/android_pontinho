package com.example.pontinho

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_add_score.*

class AddScoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_score)

        var bundle = intent.extras
        val userScore = findViewById<EditText>(R.id.addScoreText)

        findViewById<Button>(R.id.addScoreButton).setOnClickListener{
            if(userScore.text.isNullOrBlank()){

            }
            val returnIntent = Intent()
            returnIntent.putExtra("userId", bundle?.getInt("userId"))
            returnIntent.putExtra("userScore", userScore.text.toString())
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}