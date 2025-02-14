package com.yeminnaing.chatapp.data.repositories

import android.content.Context
import android.net.Uri
import com.yeminnaing.chatapp.data.network.realTimeDataBase.ProfileRealTimeDataBaseImpl
import com.yeminnaing.chatapp.domain.repositories.ProfileRepo
import com.yeminnaing.chatapp.domain.responses.UserResponse
import javax.inject.Inject

class ProfileRepoImpl @Inject constructor(
    private val profileRealTimeDataBaseImpl: ProfileRealTimeDataBaseImpl,
) : ProfileRepo {
    override fun getUserProfile(
        onSuccess: (user: UserResponse) -> Unit,
        onFailure: (String) -> Unit,
    ) {
        profileRealTimeDataBaseImpl.getUserProfile(
            onSuccess, onFailure
        )
    }

    override fun editUserProfile(
        email: String,
        name: String,
        address: String,
        bio: String,
        image:String
    ) {
        profileRealTimeDataBaseImpl.editUserProfile(email, name, address, bio,image)
    }

    override fun uploadImage(imageUri: Uri, user: UserResponse, context: Context) {
        profileRealTimeDataBaseImpl.uploadImage(imageUri, user, context)
    }
}