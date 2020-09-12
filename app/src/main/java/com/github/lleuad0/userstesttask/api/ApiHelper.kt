package com.github.lleuad0.userstesttask.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object ApiHelper {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://reqres.in/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api : UsersApi = retrofit.create(UsersApi::class.java)
}