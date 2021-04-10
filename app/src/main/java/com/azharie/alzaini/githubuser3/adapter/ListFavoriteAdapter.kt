package com.azharie.alzaini.githubuser3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azharie.alzaini.githubuser3.data.User
import com.azharie.alzaini.githubuser3.databinding.ItemFavoriteBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions



class ListFavoriteAdapter(val listener: (User) -> Unit) :
    RecyclerView.Adapter<ListFavoriteAdapter.ListFavoriteViewHolder>() {

   var listUser = ArrayList<User>()

        set(listUser) {
            if (listUser.size > 0) {
                this.listUser.clear()
            }
            this.listUser.addAll(listUser)
            notifyDataSetChanged()
        }

    fun removeItem(position: Int) {
        this.listUser.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, this.listUser.size)
    }


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        position: Int
    ): ListFavoriteAdapter.ListFavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(viewGroup.context))
        return ListFavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ListFavoriteAdapter.ListFavoriteViewHolder,
        position: Int
    ) {
        val user = listUser[position]
        holder.bindView(user, listener)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    inner class ListFavoriteViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(user: User, listener: (User) -> Unit) {
            binding.tvNamaF.text = user.username

            with(binding) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(65, 65))
                    .into(imgAvatarF)
            }
            binding.tvUrlF.text = user.user_url
            itemView.setOnClickListener { listener(user) }

        }

    }
}