package com.yeminnaing.chatapp.presentation.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.yeminnaing.chatapp.data.network.auth.AuthManagerImpl
import com.yeminnaing.chatapp.data.network.realTimeDataBase.ChannelRealTimeDataBaseImpl
import com.yeminnaing.chatapp.data.network.realTimeDataBase.MessageRealtimeDataBaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFireBaseAuthReference(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthManager(firebaseAuth: FirebaseAuth): AuthManagerImpl {
        return AuthManagerImpl(firebaseAuth)
    }


    @Provides
    @Singleton
    fun provideFireBaseDataBase(): FirebaseDatabase {
        return Firebase.database
    }

    @Provides
    @Singleton
    fun provideChannelRealTimeDataBaseImpl(firebase: FirebaseDatabase): ChannelRealTimeDataBaseImpl {
        return ChannelRealTimeDataBaseImpl(
            firebase
        )
    }

    @Provides
    @Singleton
    fun provideMessageRealTimeDataBaseImpl(firebase: FirebaseDatabase,firebaseAuth: FirebaseAuth): MessageRealtimeDataBaseImpl {
        return MessageRealtimeDataBaseImpl(
            firebase,firebaseAuth
        )
    }
}