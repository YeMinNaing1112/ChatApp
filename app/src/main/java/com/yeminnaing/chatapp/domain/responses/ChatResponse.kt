package com.yeminnaing.chatapp.domain.responses

data class ChatResponse(
    val chatId :String="",
    val lastMessage:String="",
    val participants:Map<String,Boolean> = emptyMap()
)

