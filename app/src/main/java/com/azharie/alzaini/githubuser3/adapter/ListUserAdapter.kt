package com.azharie.alzaini.githubuser3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azharie.alzaini.githubuser3.data.User
import com.azharie.alzaini.githubuser3.databinding.ItemUsersBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ListUserAdapter(val listener: (User) -> Unit) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
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
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(viewGroup.context))
        return ListViewHolder(binding)


    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        holder.bindView(user, listener)

    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    inner class ListViewHolder(private val binding: ItemUsersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(user: User, listener: (User) -> Unit) {
            binding.tvNama.text = user.username

            with(binding){
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(65,65))
                    .into(imgAvatar)

            }
            binding.tvUrl.text = user.user_url
            itemView.setOnClickListener { listener(user)}

        }
    }
}