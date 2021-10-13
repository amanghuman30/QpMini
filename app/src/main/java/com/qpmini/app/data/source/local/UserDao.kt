package com.qpmini.app.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.qpmini.app.data.models.User
import java.util.*

@Dao
interface UserDao {

    //Insert/Get Users
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipant(user: User) : Long

    @Query("Select * from users where id=:userId")
    fun getUser(userId : UUID) : LiveData<User>

}