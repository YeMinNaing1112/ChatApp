package com.yeminnaing.chatapp.domain.responses

data class ChannelResponse(
    val id: String = "",
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
)
