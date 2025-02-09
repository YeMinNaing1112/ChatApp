package com.yeminnaing.chatapp.data.repositories

import android.content.Context
import com.yeminnaing.chatapp.data.network.realTimeDataBase.MessageRealtimeDataBaseImpl
import com.yeminnaing.chatapp.domain.repositories.MessageRepo
import com.yeminnaing.chatapp.domain.responses.Message
import com.yeminnaing.chatapp.domain.responses.MessageResponse
import javax.inject.Inject

class MessageRepoImpl @Inject constructor(
    private val messageRealtimeDataBaseImpl: MessageRealtimeDataBaseImpl,
) : MessageRepo {
    override fun sendMessage(chatId: String, message: String) {
        messageRealtimeDataBaseImpl.sendMessage(chatId, message)
    }

    override fun listenForMessage(
        onSuccess: (messages: List<Message>) -> Unit,
        onFailure: (String) -> Unit,
        chatId: String,
    ) {
        messageRealtimeDataBaseImpl.listenForMessage(onSuccess, onFailure, chatId)
    }

    override fun getLastMessage(
        onSuccess: (messages: MessageResponse) -> Unit,
        onFailure: (String) -> Unit,
        chatId: String,
    ) {
        messageRealtimeDataBaseImpl.getLastMessage(onSuccess, onFailure, chatId)
    }

//    override fun createMessage(chatId: String) {
//        messageRealtimeDataBaseImpl.createMessage(chatId)
//    }

    //    override suspend fun subscribeToTopic(topic: String) {
//       messageRealtimeDataBaseImpl.subscribeToTopic(topic)
//    }
//    suspend fun getLastMessage(chatId: String): Result<MessageResponse> {
//        return messageRealtimeDataBaseImpl.getLastMessageSuspend( chatId)
//    }
}