package com.qpmini.app.api

import com.qpmini.app.data.models.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {

    @GET("/profile/{profileId}")
    suspend fun getUser(@Path("profileId") userId: String): Response<User>

}