package com.yeminnaing.chatapp.data.repositories

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
}