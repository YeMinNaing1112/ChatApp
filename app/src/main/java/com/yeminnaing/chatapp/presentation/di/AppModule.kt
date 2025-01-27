package com.yeminnaing.chatapp.presentation.di

import android.app.Application
import android.content.Context
import androidx.work.WorkManager
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.messaging.FirebaseMessaging
import com.yeminnaing.chatapp.R
import com.yeminnaing.chatapp.data.network.ApiServices
import com.yeminnaing.chatapp.data.network.auth.AuthManagerImpl
import com.yeminnaing.chatapp.data.network.realTimeDataBase.ChatsRealTimeDataBaseImpl
import com.yeminnaing.chatapp.data.network.realTimeDataBase.MessageRealtimeDataBaseImpl
import com.yeminnaing.chatapp.data.notificationManager.NotificationService
import com.yeminnaing.chatapp.presentation.navigation.DefaultNavigator
import com.yeminnaing.chatapp.presentation.navigation.Destination
import com.yeminnaing.chatapp.presentation.navigation.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
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
    fun provideFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()


    @Provides
    @Singleton
    fun provideMessageRealTimeDataBaseImpl(
        firebase: DatabaseReference,
        firebaseAuth: FirebaseAuth,
        firebaseMessaging: FirebaseMessaging,
        apiServices: ApiServices
    ): MessageRealtimeDataBaseImpl {
        return MessageRealtimeDataBaseImpl(
            firebase, firebaseAuth,firebaseMessaging,apiServices
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
    @Singleton
    fun provideNotificationService(
        context: Context,
    ): NotificationService {
        return NotificationService(context = context)
    }

    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(
        context: Context
    ):OkHttpClient{
        val loggingInterceptor =  HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val authInterceptor = Interceptor{chain->
            val accessToken = getAccessToken(context)
            val newRequest:Request= chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(newRequest)
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ):Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }


    private fun getAccessToken(context: Context): String {
        val inputStream: InputStream = context.resources.openRawResource(R.raw.chat_key)
        val googleCredentials = GoogleCredentials.fromStream(inputStream)
            .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
        return googleCredentials.refreshAccessToken().tokenValue
    }

    @Provides
    @Singleton
    fun provideFcmApi(retrofit: Retrofit):ApiServices{
        return retrofit.create(ApiServices::class.java)
    }

}



 
