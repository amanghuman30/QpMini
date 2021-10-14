package com.qpmini.app.data.source

import androidx.lifecycle.LiveData
import com.qpmini.app.data.models.ChatResponse
import com.qpmini.app.data.models.Chats
import com.qpmini.app.data.models.Messages
import com.qpmini.app.data.models.User
import com.qpmini.app.util.Resource
import retrofit2.Response
import java.util.*

interface ChatDataSource {
    suspend fun getCurrentChatUser(id : UUID): Resource<User?>
    suspend fun getChats(): Resource<List<Chats>>
    suspend fun getMessages(id : Int) : List<Messages>
    suspend fun saveUser(user: User)
    suspend fun saveChat(chats: Chats) : Long
    suspend fun saveMessage(messages: Messages)
    suspend fun sendMessage(userId : String, message : String) : Response<ChatResponse>
}