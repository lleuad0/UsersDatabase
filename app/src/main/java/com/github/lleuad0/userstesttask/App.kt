package com.github.lleuad0.userstesttask

import android.app.Application
import androidx.room.Room
import com.github.lleuad0.userstesttask.database.UsersDatabase
import com.github.lleuad0.userstesttask.database.databaseName
import timber.log.Timber


lateinit var appDatabase: UsersDatabase

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        appDatabase = Room.databaseBuilder(
            this,
            UsersDatabase::class.java,
            databaseName
        ).build()


        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}