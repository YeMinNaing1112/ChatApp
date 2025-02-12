package com.yeminnaing.chatapp.domain.repositories

import com.yeminnaing.chatapp.domain.responses.UserResponse

interface ProfileRepo{
    fun getUserProfile(
        onSuccess: (user: UserResponse) -> Unit,
        onFailure: (String) -> Unit,
    )
    fun editUserProfile(
        email: String,
        name: String,
        address:String,
        bio:String,
    )
}