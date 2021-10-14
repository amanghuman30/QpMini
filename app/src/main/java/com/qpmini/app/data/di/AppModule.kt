package com.qpmini.app.data.di

import android.content.Context
import androidx.room.Room
import com.qpmini.app.data.source.ChatDataSource
import com.qpmini.app.data.source.local.ChatDatabase
import com.qpmini.app.data.source.local.ChatLocalDataSource
import com.qpmini.app.data.source.remote.ChatRemoteDataSource
import com.qpmini.app.dispatchers.ChatTaskDispatchers
import com.qpmini.app.dispatchers.TaskDispatchers
import com.qpmini.app.repositories.ChatRepository
import com.qpmini.app.repositories.Repository
import com.qpmini.app.util.CHAT_DATABASE_NAME
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteSource

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun providesChatDatabase(@ApplicationContext context: Context) : ChatDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ChatDatabase::class.java,
            CHAT_DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    @LocalSource
    fun providesLocalDatSource(chatDatabase: ChatDatabase) : ChatDataSource = ChatLocalDataSource(chatDatabase)

    @Provides
    @Singleton
    @RemoteSource
    fun providesRemoteDatSource() : ChatDataSource = ChatRemoteDataSource()

    @Provides
    @Singleton
    fun providesChatRepository(
        @LocalSource localDataSource: ChatDataSource,
        @RemoteSource remoteSource: ChatDataSource
    ) : Repository = ChatRepository(localDataSource, remoteSource)

    @Provides
    @Singleton
    fun providesTaskDispatchers() : TaskDispatchers = ChatTaskDispatchers()

}