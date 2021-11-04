package com.example.sqlite.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase

class DataManager {

    companion object {
        val instance = DataManager()
    }

    var dbHelper: SQLiteDatabase? = null

    fun getDB(context: Context): SQLiteDatabase {
        if (dbHelper == null)
            dbHelper = DBHelper(context).readableDatabase

        return dbHelper!!
    }
}