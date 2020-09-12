package com.github.lleuad0.userstesttask.database

import androidx.room.Database
import androidx.room.RoomDatabase


const val databaseName = "users_database"

@Database(entities = [UserEntity::class], version = 1)
abstract class UsersDatabase: RoomDatabase() {

    abstract fun userDao():UsersDao

}