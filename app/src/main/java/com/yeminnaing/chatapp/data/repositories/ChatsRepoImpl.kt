package com.yeminnaing.chatapp.data.repositories

import com.yeminnaing.chatapp.data.network.realTimeDataBase.ChatsRealTimeDataBaseImpl
import com.yeminnaing.chatapp.domain.repositories.ChatsRepo
import com.yeminnaing.chatapp.domain.responses.ChatResponse
import com.yeminnaing.chatapp.domain.responses.UserResponse
import javax.inject.Inject

class ChatsRepoImpl @Inject constructor(
private val dataBaseImpl: ChatsRealTimeDataBaseImpl,
):ChatsRepo {
    override fun getChats(
        onSuccess: (List<ChatResponse>) -> Unit,
        onFailure: (String) -> Unit,
    ) {
        dataBaseImpl.getChats( onSuccess, onFailure)
    }


    override fun  createChat( chatId:String, targetUserId:String) {
        dataBaseImpl.createChat( chatId,targetUserId)
    }

    override fun findUserByEmail(
        email: String,
        onSuccess: (UserResponse) -> Unit,
        onFailure: (String) -> Unit,
    ) {
        dataBaseImpl.findUserByEmail(email, onSuccess, onFailure)
    }

    override fun findUserByName(
        name: String,
        onSuccess: (List<UserResponse>) -> Unit,
        onFailure: (String) -> Unit,
    ) {
       dataBaseImpl.findUserByName(name, onSuccess, onFailure)
    }


}