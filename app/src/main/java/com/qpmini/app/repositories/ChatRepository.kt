package com.qpmini.app.repositories

import com.qpmini.app.data.di.LocalSource
import com.qpmini.app.data.di.RemoteSource
import com.qpmini.app.data.source.ChatDataSource
import javax.inject.Inject

class ChatRepository (
    private  val localDataSource : ChatDataSource,
    private val remoteDataSource: ChatDataSource
    ): Repository {



}