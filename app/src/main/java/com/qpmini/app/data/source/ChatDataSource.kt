package com.qpmini.app.data.source

import androidx.lifecycle.LiveData
import com.qpmini.app.data.models.Chats
import com.qpmini.app.data.models.Messages
import com.qpmini.app.data.models.User
import com.qpmini.app.util.Resource
import java.util.*

interface ChatDataSource {
    suspend fun getCurrentChatUser(id : UUID): Resource<User?>
    suspend fun getChats(): Resource<List<Chats>>
    suspend fun getMessages(id : Int) : LiveData<List<Messages>>
    suspend fun saveUser(user: User)
    suspend fun saveChat(chats: Chats)
    suspend fun saveMessage(messages: Messages)
}