package com.yeminnaing.chatapp.data.network.realTimeDataBase

import com.yeminnaing.chatapp.domain.responses.Message
import com.yeminnaing.chatapp.domain.responses.MessageResponse


interface MessageFireBaseApi {
    fun sendMessage(chatId: String, message: String)

    fun listenForMessage(
        onSuccess: (messages: List<Message>) -> Unit,
        onFailure: (String) -> Unit,
        chatId: String,
    )

    fun getLastMessage(
        onSuccess: (messages: MessageResponse) -> Unit,
        onFailure: (String) -> Unit,
        chatId: String,
    )

    fun createMessage(
        chatId: String
    )
}