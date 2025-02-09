package com.yeminnaing.chatapp.domain.repositories

import com.yeminnaing.chatapp.domain.responses.ChatResponse
import com.yeminnaing.chatapp.domain.responses.UserResponse

interface ChatsRepo {
    //    fun getChannels(onSuccess: (channels:List<ChannelResponse>) -> Unit, onFailure: (String) -> Unit)
    fun getChats(
        onSuccess: (List<ChatResponse>) -> Unit,
        onFailure: (String) -> Unit,
    )

   fun  createChat( chatId:String, targetUserId:String)

    fun findUserByEmail(
        email: String,
        onSuccess: (UserResponse) -> Unit,
        onFailure: (String) -> Unit,
    )

    fun findUserByName(
        name:String,
        onSuccess: (List<UserResponse>) -> Unit,
        onFailure: (String) -> Unit
    )



}

