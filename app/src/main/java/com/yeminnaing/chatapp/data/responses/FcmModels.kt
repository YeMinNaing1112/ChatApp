package com.yeminnaing.chatapp.data.responses

data class FcmRequestBody(
    val message:Message
)

data class Message(
    val topic:String,
    val notification:Notification,
    val data:Map<String,String>?=null
)

data class Notification(
    val title:String,
    val body:String
)

data class FcmResponse(
    val name:String?
)
