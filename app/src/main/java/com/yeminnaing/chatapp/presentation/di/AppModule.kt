package com.yeminnaing.chatapp.presentation.di

import android.app.Application
import android.content.Context
import androidx.work.WorkManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.yeminnaing.chatapp.data.network.auth.AuthManagerImpl
import com.yeminnaing.chatapp.data.network.realTimeDataBase.ChatsRealTimeDataBaseImpl
import com.yeminnaing.chatapp.data.network.realTimeDataBase.MessageRealtimeDataBaseImpl
import com.yeminnaing.chatapp.presentation.navigation.DefaultNavigator
import com.yeminnaing.chatapp.presentation.navigation.Destination
import com.yeminnaing.chatapp.presentation.navigation.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideAuthManager(
        firebaseAuth: FirebaseAuth,
        firebaseDatabase: DatabaseReference,
    ): AuthManagerImpl {
        return AuthManagerImpl(firebaseAuth, firebaseDatabase)
    }


    @Provides
    fun provideFireBaseDataBaseReference(): DatabaseReference {
        return Firebase.database.reference
    }


    @Provides
    @Singleton
    fun provideChatRealTimeDataBaseImpl(
        firebase: DatabaseReference,
        firebaseAuth: FirebaseAuth,
    ): ChatsRealTimeDataBaseImpl {
        return ChatsRealTimeDataBaseImpl(
            firebase, firebaseAuth
        )
    }


    @Provides
    @Singleton
    fun provideMessageRealTimeDataBaseImpl(
        firebase: DatabaseReference,
        firebaseAuth: FirebaseAuth,
    ): MessageRealtimeDataBaseImpl {
        return MessageRealtimeDataBaseImpl(
            firebase, firebaseAuth
        )
    }


    @Provides
    @Singleton
    fun provideNavigator(): Navigator {
        return DefaultNavigator(startDestination = Destination.AuthGraph)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }


    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}



 
