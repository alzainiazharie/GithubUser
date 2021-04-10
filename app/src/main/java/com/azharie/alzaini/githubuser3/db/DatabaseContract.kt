package com.azharie.alzaini.githubuser3.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.azharie.alzaini.githubuser3"
    const val SCHEME = "content"

    internal class FavoriteColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val AVATAR = "avatar"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
            const val LOCATION = "location"
            const val COMPANY = "company"
            const val REPOSITORY = "repository"
            const val USER_URL = "user_url"
            const val NAME = "name"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()

        }


    }

}