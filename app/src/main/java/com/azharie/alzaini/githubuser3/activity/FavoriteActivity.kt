package com.azharie.alzaini.githubuser3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.azharie.alzaini.githubuser3.R
import com.azharie.alzaini.githubuser3.activity.DetailActivity.Companion.EXTRA_POSITION
import com.azharie.alzaini.githubuser3.activity.DetailActivity.Companion.RESULT_DELETE
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
        private const val USERNAME = "username"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "FAVORITES LIST"

        binding.rvUserF.layoutManager = LinearLayoutManager(this)
        binding.rvUserF.setHasFixedSize(true)
        adapter = ListFavoriteAdapter(){
/*
*             val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(USERNAME, it)
            startActivity(intent)
* */
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(USERNAME, it)
            startActivity(intent)
        }
        binding.rvUserF.adapter = adapter






    }

    override fun onResume() {
        super.onResume()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null){
            when (requestCode){
                RESULT_DELETE -> {
                    val position = data.getIntExtra(EXTRA_POSITION, 0)
                    adapter.removeItem(position)
                    Log.d("HAPUS: ","BERHASIL HAPUS DATA")
                    Toast.makeText(this,"Berhasil",Toast.LENGTH_SHORT).show()

                }

            }
        }
    }
  /*  override fun onBackPressed() {
        val intentM = Intent(this, MainActivity::class.java)
        startActivity(intentM)
    }*/


}