package com.yeminnaing.chatapp.presentation.di

import com.yeminnaing.chatapp.data.repositories.AuthRepoImpl
import com.yeminnaing.chatapp.data.repositories.ChannelsRepoImpl
import com.yeminnaing.chatapp.data.repositories.MessageRepoImpl
import com.yeminnaing.chatapp.domain.repositories.AuthRepo
import com.yeminnaing.chatapp.domain.repositories.ChannelsRepo
import com.yeminnaing.chatapp.domain.repositories.MessageRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    abstract fun bindAuthRepoImpl(
        mAuthRepoImpl: AuthRepoImpl,
    ): AuthRepo

    @Binds
    abstract fun bindChannelsRepoImpl(
        mChannelsRepoImpl: ChannelsRepoImpl,
    ): ChannelsRepo

    @Binds
    abstract fun bindMessageRepoImpl(
        mMessageRepoImpl: MessageRepoImpl,
    ): MessageRepo
}