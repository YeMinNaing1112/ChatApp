package com.yeminnaing.chatapp.domain.repositories

import com.yeminnaing.chatapp.domain.responses.MessageResponse

interface MessageRepo {
    fun sendMessage(chatId: String, message: String)

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
}