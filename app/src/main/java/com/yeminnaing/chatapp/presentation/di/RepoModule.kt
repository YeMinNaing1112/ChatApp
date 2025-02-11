package com.yeminnaing.chatapp.presentation.di

import com.yeminnaing.chatapp.data.repositories.AuthRepoImpl
import com.yeminnaing.chatapp.data.repositories.ChatsRepoImpl
import com.yeminnaing.chatapp.data.repositories.MessageRepoImpl
import com.yeminnaing.chatapp.data.repositories.ProfileRepoImpl
import com.yeminnaing.chatapp.domain.repositories.AuthRepo
import com.yeminnaing.chatapp.domain.repositories.ChatsRepo
import com.yeminnaing.chatapp.domain.repositories.MessageRepo
import com.yeminnaing.chatapp.domain.repositories.ProfileRepo
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
        mChannelsRepoImpl: ChatsRepoImpl,
    ): ChatsRepo

    @Binds
    abstract fun bindMessageRepoImpl(
        mMessageRepoImpl: MessageRepoImpl,
    ): MessageRepo

    @Binds
    abstract fun bindProfileRepoImpl(
        mProfileRepoImpl: ProfileRepoImpl
    ):ProfileRepo
}