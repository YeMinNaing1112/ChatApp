package com.yeminnaing.chatapp.data.network.realTimeDataBase

import com.yeminnaing.chatapp.domain.responses.UserResponse

interface ProfileFireBaseApi {
    fun getUserProfile(
        onSuccess: (user: UserResponse) -> Unit,
        onFailure: (String) -> Unit,
    )
}