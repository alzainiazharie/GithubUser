package com.azharie.alzaini.consumerapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        var username: String? = null,
        var avatar: String? = null,
        var followers: Int = 0,
        var following: Int = 0,
        var location: String? = null,
        var company: String? = null,
        var repository: Int = 0,
        var user_url: String? = null,
        var name: String? = null

) : Parcelable



