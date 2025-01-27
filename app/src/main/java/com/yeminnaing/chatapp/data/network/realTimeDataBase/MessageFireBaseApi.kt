package com.yeminnaing.chatapp.data.network.realTimeDataBase

import android.content.Context
import com.google.auth.oauth2.AccessToken
import com.google.firebase.database.DatabaseReference
import com.yeminnaing.chatapp.domain.responses.MessageResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi


interface MessageFireBaseApi {
    fun sendMessage(chatId: String, message: String,context: Context)

    fun listenForMessage(
        onSuccess: (messages: List<MessageResponse>) -> Unit,
        onFailure: (String) -> Unit,
        chatId: String
    )

    fun getLastMessage(
        onSuccess: (messages: MessageResponse) -> Unit,
        onFailure: (String) -> Unit,
        chatId: String
    )
//    suspend fun subscribeToTopic(topic:String)


//    @OptIn(ExperimentalCoroutinesApi::class)
//    suspend fun getLastMessageSuspend(
//        firebaseDatabase: DatabaseReference,
//        chatId: String
//    ): Result<MessageResponse>
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    suspend fun getLastMessageSuspend(chatId: String): Result<MessageResponse>

}