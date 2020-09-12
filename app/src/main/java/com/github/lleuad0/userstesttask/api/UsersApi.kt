package com.github.lleuad0.userstesttask.api

import io.reactivex.Observable
import retrofit2.http.GET


interface UsersApi {
    @GET("/api/users?page=2")
    fun getServerResponse(): Observable<ServerResponse>

}