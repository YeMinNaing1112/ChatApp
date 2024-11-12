package com.yeminnaing.chatapp.domain.responses

data class ChatResponse(
    val chatId :String="",
    val participants:Map<String,Boolean> = emptyMap()
)

