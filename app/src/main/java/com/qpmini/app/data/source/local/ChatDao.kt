package com.qpmini.app.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.qpmini.app.data.models.Chats
import com.qpmini.app.data.models.Messages
import com.qpmini.app.data.models.User
import java.util.*

@Dao
interface ChatDao {

    //Insert/Get Chats
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: Chats) : Long

    @Query("Select * from chats")
    suspend fun getChat() : List<Chats>

    //Insert/Get Messages
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(messages: Messages) : Long

    @Query("Select * from messages where chatId=:chatId")
    fun getMessagesWithChatId(chatId : Int) : List<Messages>
}