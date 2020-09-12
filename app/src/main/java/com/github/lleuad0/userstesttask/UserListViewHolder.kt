package com.github.lleuad0.userstesttask

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.lleuad0.userstesttask.database.UserEntity
import com.github.lleuad0.userstesttask.databinding.UserListItemBinding


class UserListViewHolder(
    itemView: View,
    private val listener: UserListAdapter.Listener,
    private val binding: UserListItemBinding
) :
    RecyclerView.ViewHolder(itemView) {

    var id = 0

    init {
        binding.root.setOnClickListener { listener.onUserCardClick(id) }
    }

    fun bind(userEntity: UserEntity) {
        id = userEntity.id
        binding.cardName.text = "${userEntity.firstName} ${userEntity.lastName}"
        binding.cardEmail.text = userEntity.email
        Glide.with(binding.root).load(userEntity.avatarPath).into(binding.cardImage)
    }
}