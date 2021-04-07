package com.azharie.alzaini.githubuser3.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.azharie.alzaini.githubuser3.R
import com.azharie.alzaini.githubuser3.adapter.ListFavoriteAdapter
import com.azharie.alzaini.githubuser3.databinding.ActivityFavoriteBinding
import com.azharie.alzaini.githubuser3.db.UserHelper
import com.azharie.alzaini.githubuser3.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: ListFavoriteAdapter

    companion object{
        private const val EXTRA_STATE = "EXTRA_STATE"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "FAVORITES LIST"

        binding.rvUserF.layoutManager = LinearLayoutManager(this)
        binding.rvUserF.setHasFixedSize(true)
        adapter = ListFavoriteAdapter(){

        }
        binding.rvUserF.adapter = adapter

        loadUserSync()


    }

    private fun loadUserSync(){
        GlobalScope.launch(Dispatchers.Main) {
            //ada progress bar
            val userHelper = UserHelper.getInstance(applicationContext)
            userHelper.open()
            val defferedUsers = async(Dispatchers.IO){
                val cursor = userHelper.quearyAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            //progress bar

            val users = defferedUsers.await()
            if (users.size > 0){
                adapter.listUser = users
            } else {
                adapter.listUser = ArrayList()
                Log.d("TAMPIL_FAVORITE", "Data tidak ada")
            }
            //aku liat org pada close di apus :)
            userHelper.close()

        }

    }


}