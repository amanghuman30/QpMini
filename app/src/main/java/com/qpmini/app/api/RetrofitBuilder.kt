package com.qpmini.app.api

import com.qpmini.app.util.BASE_URL
import com.qpmini.app.util.EmptyConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {

    companion object {

        private val retrofit by lazy {

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(EmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val getUserApi by lazy {
            retrofit.create(UserApi::class.java)
        }

        val getChatApi by lazy {
            retrofit.create(ChatApi::class.java)
        }
    }
}