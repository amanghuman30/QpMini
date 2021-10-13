package com.qpmini.app.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.qpmini.app.data.models.Chats
import com.qpmini.app.data.models.Messages
import com.qpmini.app.data.models.User
import java.util.*

@Dao
interface ChatDao {

    //Insert/Get Users
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipant(user: User) : Long

    @Query("Select * from users where id=:userId")
    fun getUser(userId : UUID) : LiveData<User>

    //Insert/Get Chats
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: Chats) : Long

    @Query("Select * from chats where participantId=:userId")
    fun getChat(userId : UUID) : LiveData<Chats>

    //Insert/Get Messages
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(messages: Messages) : Long

    @Query("Select * from messages where chatId=:chatId")
    fun getMessagesWithChatId(chatId : Int) : LiveData<List<Messages>>
}