package com.qpmini.app.api

import com.qpmini.app.data.models.ChatResponse
import com.qpmini.app.data.models.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {

    @POST("/chat/{profileId}")
    suspend fun sendMessage(@Path("profileId") userId: String, @Body requestBody: RequestBody): Response<ChatResponse>

}