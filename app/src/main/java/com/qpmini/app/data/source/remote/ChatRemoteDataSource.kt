package com.qpmini.app.data.source.remote

import androidx.lifecycle.LiveData
import com.qpmini.app.api.RetrofitBuilder
import com.qpmini.app.data.models.Chats
import com.qpmini.app.data.models.Messages
import com.qpmini.app.data.models.User
import com.qpmini.app.data.source.ChatDataSource
import com.qpmini.app.util.Resource
import java.util.*
import javax.inject.Singleton

@Singleton
class ChatRemoteDataSource : ChatDataSource{

    override suspend fun getChats(): Resource<List<Chats>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMessages(id : Int): LiveData<List<Messages>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun saveChat(chats: Chats) {
        TODO("Not yet implemented")
    }

    override suspend fun saveMessage(messages: Messages) {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentChatUser(id : UUID): Resource<User?> {
         val result = RetrofitBuilder.getUserApi.getUser(id.toString())
        return if(result.isSuccessful) {
            Resource.Success(result.body())
        } else {
            Resource.Error(result.message())
        }
    }

}