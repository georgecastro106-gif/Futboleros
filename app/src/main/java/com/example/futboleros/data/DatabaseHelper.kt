package com.example.futboleros.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "FutbolerosDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE favorites (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, rating REAL)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS favorites")
        onCreate(db)
    }

    fun addFavorite(name: String, rating: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("rating", rating)
        }
        val result = db.insert("favorites", null, values)
        db.close()
        return result != -1L
    }
}

