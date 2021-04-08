package com.azharie.alzaini.githubuser3.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWING
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.NAME
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.USER_URL
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion._ID

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbuserapp"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                "($USERNAME TEXT PRIMARY KEY," +
                "$AVATAR TEXT," +
                "$FOLLOWERS INTEGER," +
                "$FOLLOWING INTEGER," +
                "$LOCATION TEXT," +
                "$COMPANY TEXT," +
                "$REPOSITORY INTEGER," +
                "$USER_URL TEXT," +
                "$NAME TEXT)"

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


}