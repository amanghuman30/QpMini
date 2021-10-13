package com.qpmini.app.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.qpmini.app.data.models.Chats
import com.qpmini.app.data.models.Messages
import com.qpmini.app.data.models.User

@Database(
    entities = [User::class, Chats::class, Messages::class],
    version = 1
)
@TypeConverters(UUIDConverter::class, DateConverter::class, MessageTypeConverter::class)
abstract class ChatDatabase : RoomDatabase(){

    abstract fun getChatDao() : ChatDao

}