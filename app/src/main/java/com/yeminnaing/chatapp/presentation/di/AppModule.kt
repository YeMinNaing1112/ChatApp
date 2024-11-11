package com.yeminnaing.chatapp.presentation.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.yeminnaing.chatapp.data.network.auth.AuthManagerImpl
import com.yeminnaing.chatapp.data.network.realTimeDataBase.ChatsRealTimeDataBaseImpl
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
    fun provideAuthManager(firebaseAuth: FirebaseAuth,firebaseDatabase: DatabaseReference): AuthManagerImpl {
        return AuthManagerImpl(firebaseAuth,firebaseDatabase)
    }


    @Provides
    fun provideFireBaseDataBaseReference(): DatabaseReference {
        return Firebase.database.reference
    }



    @Provides
    @Singleton
    fun provideChatRealTimeDataBaseImpl(firebase: DatabaseReference,firebaseAuth: FirebaseAuth): ChatsRealTimeDataBaseImpl {
        return ChatsRealTimeDataBaseImpl(
            firebase,firebaseAuth
        )
    }



    @Provides
    @Singleton
    fun provideMessageRealTimeDataBaseImpl(firebase: DatabaseReference,firebaseAuth: FirebaseAuth): MessageRealtimeDataBaseImpl {
        return MessageRealtimeDataBaseImpl(
            firebase,firebaseAuth
        )
    }
}



 
