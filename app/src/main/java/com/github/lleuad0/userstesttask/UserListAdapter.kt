package com.github.lleuad0.userstesttask

import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.lleuad0.userstesttask.database.UserEntity
import com.github.lleuad0.userstesttask.databinding.UserListItemBinding


class UserListAdapter : RecyclerView.Adapter<UserListViewHolder>() {

    interface Listener{
        fun onUserCardClick(id: Int)
    }
    lateinit var listener: Listener

    var userList: List<UserEntity> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val layoutInflater = from(parent.context)
        val binding = UserListItemBinding.inflate(layoutInflater, parent, false)
        val view = binding.root
        return UserListViewHolder(view, listener, binding)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int = userList.size
}