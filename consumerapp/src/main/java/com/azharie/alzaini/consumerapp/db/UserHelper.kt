package com.azharie.alzaini.consumerapp.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.azharie.alzaini.consumerapp.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.azharie.alzaini.consumerapp.db.DatabaseContract.FavoriteColumns.Companion.USERNAME
import java.sql.SQLException

class UserHelper(context: Context) {
    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE: UserHelper? = null
        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context): UserHelper =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: UserHelper(context)
                }
    }


    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun queryByUsername(username: String): Cursor{
        return database.query(DATABASE_TABLE, null, "$USERNAME = ?", arrayOf(username),null, null, null, null)
    }


}