package com.github.lleuad0.userstesttask.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


const val usersTable = "users_table"

@Entity(tableName = usersTable)
data class UserEntity(
    @PrimaryKey var id: Int,

    var email: String,

    @SerializedName("first_name") var firstName: String,

    @SerializedName("last_name") var lastName: String,

    @SerializedName("avatar") var avatarPath: String
)