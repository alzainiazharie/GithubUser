package com.azharie.alzaini.githubuser3.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.USERNAME
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

    fun close() {
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun quearyAll(): Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "$USERNAME ASC")
    }

    fun queryByUsername(username: String): Cursor{
        return database.query(DATABASE_TABLE, null, "$USERNAME = ?", arrayOf(username),null, null, null, null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(username: String): Int{
        return database.delete(DATABASE_TABLE, "$USERNAME = '$username'", null)
    }


}