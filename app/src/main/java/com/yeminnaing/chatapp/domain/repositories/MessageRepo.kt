package com.yeminnaing.chatapp.domain.repositories

import android.content.Context
import com.yeminnaing.chatapp.domain.responses.Message
import com.yeminnaing.chatapp.domain.responses.MessageResponse

interface MessageRepo {
    fun sendMessage(chatId: String, message: String)

    fun listenForMessage(
        onSuccess: (messages: List<Message>) -> Unit,
        onFailure: (String) -> Unit,
        chatId: String
    )
    fun getLastMessage(
        onSuccess: (messages: MessageResponse) -> Unit,
        onFailure: (String) -> Unit,
        chatId: String
    )

//    suspend fun subscribeToTopic(topic:String)

//    fun createMessage(chatId: String)
}