package com.qpmini.app.data.source.remote

import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.qpmini.app.api.RetrofitBuilder
import com.qpmini.app.data.models.ChatResponse
import com.qpmini.app.data.models.Chats
import com.qpmini.app.data.models.Messages
import com.qpmini.app.data.models.User
import com.qpmini.app.data.source.ChatDataSource
import com.qpmini.app.util.BODY_KEY
import com.qpmini.app.util.Resource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import java.util.*
import javax.inject.Singleton

@Singleton
class ChatRemoteDataSource : ChatDataSource{

    override suspend fun getCurrentChatUser(id : UUID): Resource<User?> {
        val result = RetrofitBuilder.getUserApi.getUser(id.toString())
        return if(result.isSuccessful) {
            Resource.Success(result.body())
        } else {
            Resource.Error(result.message())
        }
    }

    override suspend fun sendMessage(userId : String, message: String) : Response<ChatResponse> {
        // Create JSON using JSONObject
        val jsonObject = JSONObject()
        jsonObject.put(BODY_KEY, message)

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Create RequestBody
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        return RetrofitBuilder.getChatApi.sendMessage(userId, requestBody)
    }

    override suspend fun getChats(): Resource<List<Chats>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMessages(id : Int): List<Messages> {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun saveChat(chats: Chats) : Long {
        TODO("Not yet implemented")
    }

    override suspend fun saveMessage(messages: Messages) {
        TODO("Not yet implemented")
    }

}