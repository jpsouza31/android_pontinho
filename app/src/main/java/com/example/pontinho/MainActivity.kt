package com.example.pontinho

import android.R.attr
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity(), MyAdapter.OnItemClick {

    var db = DBOpenHelper(this, null)
    var photos = intArrayOf(
        R.drawable.face0,
        R.drawable.face1,
        R.drawable.face2,
        R.drawable.face3,
        R.drawable.face4
    )
    var indexPhoto = 0
    lateinit var myAdapter: MyAdapter

    val necessaryPermissions = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Permission.validPermissions(necessaryPermissions, this)

        myAdapter = MyAdapter( ArrayList(getPlayersDb().values),this, this)

        val viewManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.mainRecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = myAdapter
        }

        val addButton = findViewById<Button>(R.id.mainAddButton)
        addButton.setOnClickListener{
            startActivityForResult(Intent(this, AddUserActivity::class.java), 1)
        }
    }

    override fun onResume() {
        super.onResume()
        myAdapter.contactList = ArrayList(getPlayersDb().values)
        myAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode === Activity.RESULT_OK) {
                val result = data?.getStringExtra("userName")
                db.addPlayer(result!!, photos.get(indexPhoto))
                if(indexPhoto == photos.size) indexPhoto = 0
                else indexPhoto++
            }
            if (resultCode === Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == 2) {
            val score = data?.getStringExtra("userScore")
            val userId = data?.getIntExtra("userId", -1)
            var user = getPlayersDb().get(userId!!)
            if(user != null){
                user.score+=score!!.toInt()
                if(user.score >= 100) {
                    db.setGameOver(user.id)
                    user.score = 0
                }
                db.updatePlayerScore(user.id, user.score)
            }
        }
    }

    override fun onItemClick(user: User, position: Int) {
        var intent = Intent(this, AddScoreActivity::class.java)
        intent.putExtra("userId", user.id)
        startActivityForResult(intent, 2)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.resetGame){
            db.resetDbData()
            myAdapter.contactList.clear()
        }else if(item.itemId == R.id.resetPlayers){
            db.resetDbPlayersData()
            myAdapter.contactList = db.getAllPlayers()
        }

        myAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

    fun getPlayersDb(): HashMap<Int, User>{
        var userList = db.getAllPlayers()
        var userHash = HashMap<Int, User>()
        userList.forEach{
            userHash.put(it.id, it)
        }
        return userHash
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        grantResults.forEach {
            if(it == PackageManager.PERMISSION_DENIED)
                permissionAlert()
        }
    }

    fun permissionAlert(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Permissões Negadas")
        builder.setCancelable(false)
        builder.setMessage("Para utlizar o app é necessário aceitar as permissões")
        builder.setPositiveButton("Confirmar") { dialog, which ->
            finish()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}