package com.yeminnaing.chatapp.domain.repositories

import com.yeminnaing.chatapp.domain.responses.MessageResponse

interface MessageRepo {
    fun sendMessage(channelId: String, message: String)

    fun listenForMessage(
        onSuccess: (messages: List<MessageResponse>) -> Unit,
        onFailure: (String) -> Unit,
        channelId: String
    )
}