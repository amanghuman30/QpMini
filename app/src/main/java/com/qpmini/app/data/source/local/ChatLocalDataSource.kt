package com.qpmini.app.data.source.local

import androidx.lifecycle.LiveData
import com.qpmini.app.data.models.Chats
import com.qpmini.app.data.models.Messages
import com.qpmini.app.data.models.User
import com.qpmini.app.data.source.ChatDataSource
import com.qpmini.app.util.Resource
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatLocalDataSource @Inject constructor(
    private val db : ChatDatabase
) : ChatDataSource {

    override suspend fun getChats(): Resource<List<Chats>> {
        return Resource.Success(db.getChatDao().getChat())
    }

    override suspend fun getMessages(id : Int): LiveData<List<Messages>> {
        return db.getChatDao().getMessagesWithChatId(id)
    }

    override suspend fun saveUser(user: User) {
        db.getUserDao().insertParticipant(user)
    }

    override suspend fun saveChat(chat: Chats) {
        db.getChatDao().insertChat(chat)
    }

    override suspend fun saveMessage(message: Messages) {
        db.getChatDao().insertMessage(message)
    }

    override suspend fun getCurrentChatUser(id : UUID): Resource<User?> {
        return Resource.Success(db.getUserDao().getUser(id))
    }

}