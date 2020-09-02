package com.example.pontinho

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBOpenHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION)
{

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NAME TEXT, $COLUMN_SCORE INTEGER, $COLUMN_GAMEOVER INTEGER, $COLUMN_PHOTO INTEGER)")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addPlayer(name: String, photo: Int) {
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_SCORE, 0)
        values.put(COLUMN_GAMEOVER, 0)
        values.put(COLUMN_PHOTO, photo)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun resetDbData(): Boolean {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, null, null).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    fun resetDbPlayersData(): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_SCORE, 0)
        values.put(COLUMN_GAMEOVER, 0)
        val success = db.update(TABLE_NAME, values, null, null).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    fun updatePlayerScore(id: Int, score: Int): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ID, id)
        values.put(COLUMN_SCORE, score)
        val success = db.update(TABLE_NAME, values, COLUMN_ID + "=?", arrayOf(id.toString())).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    fun getAllPlayers(): ArrayList<User> {
        var userList = ArrayList<User>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val user = User(
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    isGameOver = cursor.getInt(cursor.getColumnIndex(COLUMN_GAMEOVER)) == 1,
                    photo = cursor.getInt(cursor.getColumnIndex(COLUMN_PHOTO)),
                    score = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORE))
                )
                userList.add(user)
            }
        }

        return userList
    }

    fun setGameOver(id: Int): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ID, id)
        values.put(COLUMN_GAMEOVER, 1)
        val success = db.update(TABLE_NAME, values, COLUMN_ID + "=?", arrayOf(id.toString())).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    companion object {
        private val DATABASE_VERSION = 3
        private val DATABASE_NAME = "pontinho"
        val TABLE_NAME = "user_score"
        val COLUMN_ID = "player_id"
        val COLUMN_NAME = "player_name"
        val COLUMN_SCORE = "player_score"
        val COLUMN_GAMEOVER = "game_over"
        val COLUMN_PHOTO = "photo"
    }

}