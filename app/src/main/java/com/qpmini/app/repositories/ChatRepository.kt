package com.qpmini.app.repositories

import androidx.lifecycle.LiveData
import com.qpmini.app.data.di.LocalSource
import com.qpmini.app.data.di.RemoteSource
import com.qpmini.app.data.models.Chats
import com.qpmini.app.data.models.Messages
import com.qpmini.app.data.models.User
import com.qpmini.app.data.source.ChatDataSource
import com.qpmini.app.util.Resource
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository (
    private  val localDataSource : ChatDataSource,
    private val remoteDataSource: ChatDataSource
    ): Repository {

    override suspend fun getChats(): Resource<List<Chats>> {
        return localDataSource.getChats()
    }

    override suspend fun getUser(id : UUID): Resource<User?> {
        return localDataSource.getCurrentChatUser(id)
    }

    override suspend fun getUserFromRemote(id : UUID) : Resource<User?>{
        return remoteDataSource.getCurrentChatUser(id)
    }

    override suspend fun getMessages(id: Int): LiveData<List<Messages>> {
        return localDataSource.getMessages(id)
    }

    override suspend fun saveUser(user: User) {
        localDataSource.saveUser(user)
    }

    override suspend fun saveChat(chats: Chats) {
        localDataSource.saveChat(chats)
    }

    override suspend fun saveMessage(messages: Messages) {
        localDataSource.saveMessage(messages)
    }
}