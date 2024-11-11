package com.yeminnaing.chatapp.domain.responses

data class MessageResponse(
    val id:String="",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val timeStamp: Long = System.currentTimeMillis(),

//    val senderImage: String? = null,
//    val imageUrl: String? = null
)
