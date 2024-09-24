package com.yeminnaing.chatapp.data.network.realTimeDataBase

import com.yeminnaing.chatapp.domain.responses.MessageResponse


interface MessageFireBaseApi {
    fun sendMessage(channelId: String, message: String)

    fun listenForMessage(
        onSuccess: (messages: List<MessageResponse>) -> Unit,
        onFailure: (String) -> Unit,
        channelId: String
    )
}