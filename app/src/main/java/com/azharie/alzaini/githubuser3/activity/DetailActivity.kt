package com.azharie.alzaini.githubuser3.activity

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.azharie.alzaini.githubuser3.R
import com.azharie.alzaini.githubuser3.adapter.SectionsPagerAdapter
import com.azharie.alzaini.githubuser3.data.User
import com.azharie.alzaini.githubuser3.databinding.ActivityDetailBinding
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWING
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.NAME
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.USER_URL
import com.azharie.alzaini.githubuser3.db.UserHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.item_favorite.*
import kotlinx.android.synthetic.main.item_users.*
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private lateinit var userHelper: UserHelper

    private var position: Int = 0



   private lateinit var uriWithId: Uri

    companion object {
        @StringRes
        private val TABS_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )

        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val RESULT_DELETE = 301

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentUsername = intent.getParcelableExtra<User>(MainActivity.USERNAME)

        getUserDetail(intentUsername?.username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)

        sectionsPagerAdapter.username = intentUsername?.username

        binding.viewPager.adapter = sectionsPagerAdapter


        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TABS_TITLES[position])

        }.attach()

        supportActionBar?.elevation = 0f


        var statusFabFavorite = false
        setStatusFabFavorite(statusFabFavorite)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()



        val cursor: Cursor = userHelper.queryByUsername(intentUsername?.username.toString())
        if (cursor.moveToNext()) {
            statusFabFavorite = true
            setStatusFabFavorite(true)
        }
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + intentUsername?.username.toString())



        binding.fabAdd.setOnClickListener {


            if (!statusFabFavorite) {


                val username = binding.detaiUsername.text.toString()
                val avatar = intentUsername?.avatar
                val followers = binding.detailFollowers.text.toString()
                val following = binding.detailFollowing.text.toString()
                val location = binding.detailLocation.text.toString()
                val company = binding.detailCompany.text.toString()
                val repository = binding.detailRepository.text.toString()
                val user_url = binding.detailUrl.text.toString()
                val name = binding.detailName.text.toString()

                val values = ContentValues()
                values.put(USERNAME, username)
                values.put(AVATAR, avatar)
                values.put(FOLLOWERS, followers)
                values.put(FOLLOWING, following)
                values.put(LOCATION, location)
                values.put(COMPANY, company)
                values.put(REPOSITORY, repository)
                values.put(USER_URL, user_url)
                values.put(NAME, name)

                contentResolver.insert(CONTENT_URI, values)
                statusFabFavorite = !statusFabFavorite
                setStatusFabFavorite(statusFabFavorite)
                Toast.makeText(this, R.string.toast_follow_, Toast.LENGTH_SHORT).show()

            } else {
                //userHelper.deleteById(intentUsername?.username.toString())

                contentResolver.delete(uriWithId, null, null)
                statusFabFavorite = !statusFabFavorite
                setStatusFabFavorite(statusFabFavorite)

                /*val intentD = Intent()
                intentD.putExtra(EXTRA_POSITION, position)
                setResult(RESULT_DELETE, intentD)*/
                Toast.makeText(this, R.string.toast_not_follow, Toast.LENGTH_SHORT).show()
                finish()
            }


        }
    }


    fun loading(state: Boolean) {
        if (state) {
            binding.progressBarDetail.visibility = View.VISIBLE
        } else {
            binding.progressBarDetail.visibility = View.GONE
        }


    }

    private fun setStatusFabFavorite(statusFabFavorite: Boolean) {
        if (statusFabFavorite) {
            fab_add.setImageResource(R.drawable.ic_baseline_favorite_24)

        } else {

            fab_add.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }


    }

    private fun getUserDetail(username: String?) {
        loading(true)


        val url = "https://api.github.com/users/$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 961fa2fbc20f6972434bfb86d78ca2f286b9a599")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {

                val result = String(responseBody)

                try {
                    val item = JSONObject(result)
                    val username = item.getString("login")
                    val avatar = item.getString("avatar_url")
                    val followers = item.getInt("followers")
                    val following = item.getInt("following")
                    val location = item.getString("location")
                    val company = item.getString("company")
                    val repository = item.getInt("public_repos")
                    val user_url = item.getString("html_url")
                    val name = item.getString("name")

                    val user = User()

                    user.username = username
                    user.avatar = avatar
                    user.followers = followers
                    user.following = following
                    user.location = location
                    user.company = company
                    user.repository = repository
                    user.user_url = user_url
                    user.name = name

                    binding.detaiUsername.text = user.username

                    with(binding) {
                        Glide.with(this@DetailActivity)
                            .load(user.avatar)
                            .apply(RequestOptions().override(100, 100))
                            .into(detailAvatar)
                    }

                    binding.detailFollowers.text = user.followers.toString()
                    binding.detailFollowing.text = user.following.toString()
                    binding.detailLocation.text = user.location
                    binding.detailCompany.text = user.company
                    binding.detailRepository.text = user.repository.toString()
                    binding.detailUrl.text = user.user_url
                    binding.detailName.text = user.name



                    Log.d("cek DETAIL", result)
                    loading(false)
                } catch (e: Exception) {
                    Log.d("Exception DETAIL", e.message.toString())
                    e.printStackTrace()

                }
            }


            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())

            }
        })


    }

}