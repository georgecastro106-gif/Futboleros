package com.example.futboleros.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "favorites.db"
        private const val DATABASE_VERSION = 2

        const val TABLE_FAVORITES = "favorites"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_POSITION = "position"
        const val COLUMN_PHOTOURL = "photoUrl"
        const val COLUMN_RATING = "rating"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
        CREATE TABLE $TABLE_FAVORITES (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NAME TEXT,
            $COLUMN_POSITION TEXT,
            $COLUMN_RATING REAL,
            $COLUMN_PHOTOURL TEXT
        )
    """.trimIndent()

        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES")
        onCreate(db)
    }

    // Método para insertar o guardar un jugador favorito
    fun addFavorite(name: String, position: String, rating: Double): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_POSITION, position)
            put(COLUMN_RATING, rating)
        }
        val result = db.insert(TABLE_FAVORITES, null, values)
        db.close()
        return result != -1L
    }
}
