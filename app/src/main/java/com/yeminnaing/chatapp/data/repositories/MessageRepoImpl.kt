package com.yeminnaing.chatapp.data.repositories

import com.yeminnaing.chatapp.data.network.realTimeDataBase.MessageRealtimeDataBaseImpl
import com.yeminnaing.chatapp.domain.repositories.MessageRepo
import com.yeminnaing.chatapp.domain.responses.MessageResponse
import javax.inject.Inject

class MessageRepoImpl @Inject constructor(
    private val messageRealtimeDataBaseImpl: MessageRealtimeDataBaseImpl,
) : MessageRepo {
    override fun sendMessage(channelId: String, message: String) {
        messageRealtimeDataBaseImpl.sendMessage(channelId, message)
    }

    override fun listenForMessage(
        onSuccess: (messages: List<MessageResponse>) -> Unit,
        onFailure: (String) -> Unit,
        channelId: String,
    ) {
        messageRealtimeDataBaseImpl.listenForMessage(onSuccess, onFailure, channelId)
    }
}