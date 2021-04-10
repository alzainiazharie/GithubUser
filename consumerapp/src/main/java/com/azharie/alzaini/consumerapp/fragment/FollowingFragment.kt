package com.azharie.alzaini.consumerapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azharie.alzaini.consumerapp.R
import com.azharie.alzaini.consumerapp.adapter.ListFollowingAdapter
import com.azharie.alzaini.consumerapp.data.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray


class FollowingFragment : Fragment() {
    val listUser = ArrayList<User>()
    private lateinit var adapter: ListFollowingAdapter
    private lateinit var progressFollowing: ProgressBar

    private var username: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(USERNAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    companion object {
        private val USERNAME = "username"

        @JvmStatic
        fun newInstance(username: String?) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString(USERNAME, username)

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val rvFollowing: RecyclerView = view.findViewById(R.id.rvFollowing)
        progressFollowing = view.findViewById(R.id.progressBarFollowing)

        getFollowings(username)
        rvFollowing.setHasFixedSize(true)
        rvFollowing.layoutManager = LinearLayoutManager(activity)
        adapter = ListFollowingAdapter()
        rvFollowing.adapter = adapter

        super.onViewCreated(view, savedInstanceState)
    }


    fun loading(state: Boolean) {
        if (state) {
           progressFollowing.visibility = View.VISIBLE
        } else {
            progressFollowing.visibility = View.GONE
        }


    }

    private fun getFollowings(username: String?){
        loading(true)
        progressFollowing.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/following"
        client.addHeader("Authorization", "token 961fa2fbc20f6972434bfb86d78ca2f286b9a599")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d("FOLLOWING FRAGMENT SUC",result)

                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username = jsonObject.getString("login")
                        val avatar = jsonObject.getString("avatar_url")

                        val user = User()

                        user.username = username
                        user.avatar = avatar

                        listUser.add(user)
                    }
                    adapter.setData(listUser)
                    Log.d("URL : ", url)
                    loading(false)

                } catch (e:Exception){
                    Log.d("FOLLOWING FRAGMENT NO","onFailure ${e.message}")
                    e.printStackTrace()


                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.d("FOLLOWING onFailure", error.message.toString())
            }
        })

    }
}