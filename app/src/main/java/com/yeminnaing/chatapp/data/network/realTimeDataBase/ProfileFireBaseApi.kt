package com.yeminnaing.chatapp.data.network.realTimeDataBase

import android.content.Context
import android.net.Uri
import com.yeminnaing.chatapp.domain.responses.UserResponse

interface ProfileFireBaseApi {
    fun getUserProfile(
        onSuccess: (user: UserResponse) -> Unit,
        onFailure: (String) -> Unit,
    )

    fun editUserProfile(
        email: String,
        name: String,
        address: String,
        bio: String,
        image:String
    )

    fun uploadImage(
        imageUri: Uri,
        user: UserResponse,
        context: Context,
    )
}