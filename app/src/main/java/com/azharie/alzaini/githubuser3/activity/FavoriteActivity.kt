package com.azharie.alzaini.githubuser3.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.azharie.alzaini.githubuser3.R
import com.azharie.alzaini.githubuser3.adapter.ListFavoriteAdapter
import com.azharie.alzaini.githubuser3.databinding.ActivityFavoriteBinding

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


    }


}