package com.example.pontinho

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AddUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        val addUserButton = findViewById<Button>(R.id.addUserButton)
        val userName = findViewById<EditText>(R.id.addUserName)

        addUserButton.setOnClickListener{
            if(userName.text.isNullOrBlank()){
                Toast.makeText(this, "Favor insira um nome válido!", Toast.LENGTH_SHORT).show()
            }else {
                val returnIntent = Intent()
                returnIntent.putExtra("userName", userName.text.toString())
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }
    }
}