package com.azharie.alzaini.githubuser3.activity

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.azharie.alzaini.githubuser3.R
import com.azharie.alzaini.githubuser3.activity.DetailActivity.Companion.EXTRA_POSITION
import com.azharie.alzaini.githubuser3.activity.DetailActivity.Companion.RESULT_DELETE
import com.azharie.alzaini.githubuser3.adapter.ListFavoriteAdapter
import com.azharie.alzaini.githubuser3.databinding.ActivityFavoriteBinding
import com.azharie.alzaini.githubuser3.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
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
        private const val USERNAME = "username"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.favorite_list)

        binding.rvUserF.layoutManager = LinearLayoutManager(this)
        binding.rvUserF.setHasFixedSize(true)
        adapter = ListFavoriteAdapter{

            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(USERNAME, it)
            startActivity(intent)
        }
        binding.rvUserF.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object  : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadUserSync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)


    }

    override fun onResume() {
        super.onResume()
        loadUserSync()
    }

    private fun loadUserSync(){
        GlobalScope.launch(Dispatchers.Main) {

            val userHelper = UserHelper.getInstance(applicationContext)
            userHelper.open()
            val defferedUsers = async(Dispatchers.IO){
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }


            val users = defferedUsers.await()
            if (users.size > 0){
                adapter.listUser = users
            } else {
                adapter.listUser = ArrayList()
                Log.d("TAMPIL_FAVORITE", "Data tidak ada")
            }

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

}