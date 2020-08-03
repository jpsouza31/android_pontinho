package com.example.pontinho

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity(), MyAdapter.OnItemClick {

    var players: ArrayList<User> = ArrayList()
    var photos = intArrayOf(
        R.drawable.face0,
        R.drawable.face1,
        R.drawable.face2,
        R.drawable.face3,
        R.drawable.face4
    )
    var indexPhoto = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addButton = findViewById<Button>(R.id.mainAddButton)
        addButton.setOnClickListener{
            startActivityForResult(Intent(this, AddUserActivity::class.java), 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode === Activity.RESULT_OK) {
                val result = data?.getStringExtra("userName")
                players.add(User(result!!, 0, photos.get(indexPhoto)))
                if(indexPhoto == photos.size) indexPhoto = 0
                else indexPhoto++
            }
            if (resultCode === Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }else if (requestCode == 2) {
            val score = data?.getStringExtra("userScore")
            val userId = data?.getIntExtra("userId", -1)
            var user = players.get(userId!!)
            players.removeAt(userId)
            user.score+=score!!.toInt()
            players.add(userId, user)
        }
    }

    override fun onResume() {
        super.onResume()
        var myAdapter = MyAdapter(players, this, this)

        val viewManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.mainRecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = myAdapter
        }
    }

    override fun onItemClick(user: User, position: Int) {
        var intent = Intent(this, AddScoreActivity::class.java)
        intent.putExtra("userId", position)
        startActivityForResult(intent, 2)
    }
}