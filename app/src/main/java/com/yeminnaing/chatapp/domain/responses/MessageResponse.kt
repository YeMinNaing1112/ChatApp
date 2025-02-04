package com.yeminnaing.chatapp.domain.responses

data class Message(
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val timeStamp: Long = System.currentTimeMillis(),

//    val senderImage: String? = null,
//    val imageUrl: String? = null
)

data class MessageResponse(
    val metaData: MetaData? = MetaData(),
    val messageList: List<Message> = emptyList(),
)


data class MetaData(
    val chatId: String = "",
    val lastMessage: String = "",
    val timeStamp: Long = System.currentTimeMillis(),
)