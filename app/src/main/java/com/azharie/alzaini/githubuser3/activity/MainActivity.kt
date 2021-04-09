package com.azharie.alzaini.githubuser3.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.azharie.alzaini.githubuser3.R
import com.azharie.alzaini.githubuser3.activity.DetailActivity
import com.azharie.alzaini.githubuser3.activity.DetailActivity.Companion.REQUEST_ADD
import com.azharie.alzaini.githubuser3.adapter.ListUserAdapter
import com.azharie.alzaini.githubuser3.data.User
import com.azharie.alzaini.githubuser3.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListUserAdapter


    companion object {
        private val TAG = MainActivity::class.java.simpleName

        const val USERNAME = "username"
    }

    val listUser = ArrayList<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAllUsers()

        binding.rvUser.setHasFixedSize(true)

        binding.rvUser.layoutManager = LinearLayoutManager(this)
        adapter = ListUserAdapter() {

            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(USERNAME, it)
            startActivity(intent)
        }
        binding.rvUser.adapter = adapter


    }

    private fun showProgress(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.hint_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {


                getSearchUserByUsername(query)

                return true


            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    getAllUsers()
                } else {
                    getSearchUserByUsername(newText)
                }

                return true
            }
        })
        return true

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.language){

            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }

        if (item.itemId == R.id.favorite){
            val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
           startActivityForResult(intent, REQUEST_ADD)
        }

        if (item.itemId == R.id.set_alarm){
            val intent = Intent(this@MainActivity, SettingAlarmActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getAllUsers() {
        showProgress(true)
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users"
        client.addHeader("Authorization", "token 961fa2fbc20f6972434bfb86d78ca2f286b9a599")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {


                val result = String(responseBody)
                Log.d(TAG, result)

                try {

                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username = jsonObject.getString("login")
                        val avatar = jsonObject.getString("avatar_url")
                        val url = jsonObject.getString("html_url")

                        val user = User()

                        user.username = username
                        user.avatar = avatar
                        user.user_url = url

                        listUser.add(user)
                    }
                    adapter.setData(listUser)
                    showProgress(false)


                } catch (e: Exception) {
                    Log.d(TAG, "onFailure ${e.message}")
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


    private fun getSearchUserByUsername(username: String) {
        showProgress(true)
        val listItems = ArrayList<User>()
        val url = "https://api.github.com/search/users?q=$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 961fa2fbc20f6972434bfb86d78ca2f286b9a599")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {

                    val result = String(responseBody)
                    Log.d("SUCCESS", result)
                    val responeseObject = JSONObject(result)

                    val items = responeseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")


                        val user = User()
                        user.username = username
                        user.avatar = avatar

                        listItems.add(user)

                    }
                    adapter.setData(listItems)
                    showProgress(false)


                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
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