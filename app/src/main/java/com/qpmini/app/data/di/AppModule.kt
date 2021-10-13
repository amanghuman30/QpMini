package com.qpmini.app.data.di

import com.qpmini.app.data.source.ChatDataSource
import com.qpmini.app.data.source.local.ChatLocalDataSource
import com.qpmini.app.data.source.remote.ChatRemoteDataSource
import com.qpmini.app.repositories.ChatRepository
import com.qpmini.app.repositories.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    @LocalSource
    fun provideLocalDatSource() : ChatDataSource = ChatLocalDataSource()

    @Provides
    @Singleton
    @RemoteSource
    fun provideRemoteDatSource() : ChatDataSource = ChatRemoteDataSource()

    @Provides
    @Singleton
    fun provideChatRepository(
        @LocalSource localDataSource: ChatDataSource,
        @RemoteSource remoteSource: ChatDataSource
    ) : Repository = ChatRepository(localDataSource, remoteSource)

}