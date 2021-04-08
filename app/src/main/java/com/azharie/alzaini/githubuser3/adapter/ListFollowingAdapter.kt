package com.azharie.alzaini.githubuser3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azharie.alzaini.githubuser3.data.User
import com.azharie.alzaini.githubuser3.databinding.ItemFollowingBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ListFollowingAdapter: RecyclerView.Adapter<ListFollowingAdapter.ListViewHolder>() {

private val listUser = ArrayList<User>()
    fun setData(items: ArrayList<User>){
        listUser.clear()
        listUser.addAll(items)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        position: Int
    ): ListViewHolder {
        val binding = ItemFollowingBinding.inflate(LayoutInflater.from(viewGroup.context))
        return ListViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        holder.bindView(user)

    }

    override fun getItemCount(): Int {
        return listUser.size

    }

    inner class ListViewHolder(private val binding: ItemFollowingBinding) :
    RecyclerView.ViewHolder(binding.root){
        fun bindView(user: User){
            binding.tvNamaFollowing.text = user.username
            with(binding){
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(55,55))
                    .into(imgAvatarFollowing)
            }
        }

    }

}