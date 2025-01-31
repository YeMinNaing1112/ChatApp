package com.yeminnaing.chatapp.data.network.realTimeDataBase

import com.yeminnaing.chatapp.domain.responses.MessageResponse


interface MessageFireBaseApi {
    fun sendMessage(chatId: String, message: String)

    fun listenForMessage(
        onSuccess: (messages: List<MessageResponse>) -> Unit,
        onFailure: (String) -> Unit,
        chatId: String,
    )

    fun getLastMessage(
        onSuccess: (messages: MessageResponse) -> Unit,
        onFailure: (String) -> Unit,
        chatId: String,
    )
}