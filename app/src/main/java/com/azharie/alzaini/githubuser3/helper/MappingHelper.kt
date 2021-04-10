package com.azharie.alzaini.githubuser3.helper

import android.database.Cursor
import com.azharie.alzaini.githubuser3.data.User
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWING
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.NAME
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.USER_URL

object MappingHelper {
    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<User>{
        val userList = ArrayList<User>()
        userCursor?.apply {
            while (moveToNext()){
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                val followers = getInt(getColumnIndexOrThrow(FOLLOWERS))
                val following = getInt(getColumnIndexOrThrow(FOLLOWING))
                val location = getString(getColumnIndexOrThrow(LOCATION))
                val company = getString(getColumnIndexOrThrow(COMPANY))
                val repository = getInt(getColumnIndexOrThrow(REPOSITORY))
                val user_url = getString(getColumnIndexOrThrow(USER_URL))
                val name = getString(getColumnIndexOrThrow(NAME))
                userList.add(User(username, avatar, followers, following, location, company, repository, user_url, name))

            }

        }
        return userList
    }

    fun mapCursorToObject(userCursor: Cursor?): User{
        var user = User()
        userCursor?.apply {
            moveToFirst()
            val username = getString(getColumnIndexOrThrow(USERNAME))
            val avatar = getString(getColumnIndexOrThrow(AVATAR))
            val followers = getInt(getColumnIndexOrThrow(FOLLOWERS))
            val following = getInt(getColumnIndexOrThrow(FOLLOWING))
            val location = getString(getColumnIndexOrThrow(LOCATION))
            val company = getString(getColumnIndexOrThrow(COMPANY))
            val repository = getInt(getColumnIndexOrThrow(REPOSITORY))
            val user_url = getString(getColumnIndexOrThrow(USER_URL))
            val name = getString(getColumnIndexOrThrow(NAME))
            user = User(username, avatar, followers, following, location, company, repository, user_url, name)
        }
        return user
    }
}