package com.qpmini.app.repositories

import androidx.lifecycle.LiveData
import com.qpmini.app.data.models.ChatResponse
import com.qpmini.app.data.models.Chats
import com.qpmini.app.data.models.Messages
import com.qpmini.app.data.models.User
import com.qpmini.app.util.Resource
import retrofit2.Response
import java.util.*

interface Repository {

    suspend fun getChats() : Resource<List<Chats>>

    suspend fun getUser(id : UUID) : Resource<User?>

    suspend fun getUserFromRemote(id : UUID) : Resource<User?>

    suspend fun getMessages(id : Int) : LiveData<List<Messages>>

    suspend fun saveUser(user: User)

    suspend fun saveChat(chats: Chats)

    suspend fun saveMessage(messages: Messages)

    suspend fun sendMessage(userId : String, messages: String) : Response<ChatResponse>

}