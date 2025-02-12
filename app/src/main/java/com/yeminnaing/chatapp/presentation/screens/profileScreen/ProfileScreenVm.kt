package com.yeminnaing.chatapp.presentation.screens.profileScreen

import androidx.lifecycle.ViewModel
import com.yeminnaing.chatapp.domain.repositories.ProfileRepo
import com.yeminnaing.chatapp.domain.responses.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileScreenVm @Inject constructor(
    private val profileRepo: ProfileRepo,
) : ViewModel() {
    private val _profileState = MutableStateFlow<ProfileDataStates>(ProfileDataStates.Empty)
    val profileState = _profileState.asStateFlow()

    init {
        getProfile()
    }

    fun editProfile(
        email: String,
        name: String,
        address: String,
        bio: String,
    ) {
     profileRepo.editUserProfile(email, name, address, bio)
    }


    private fun getProfile() {
        _profileState.value = ProfileDataStates.Empty
        profileRepo.getUserProfile(
            onSuccess = {
                _profileState.value = ProfileDataStates.Success(it)
            }, onFailure = {
                _profileState.value = ProfileDataStates.Error(it)
            }
        )
    }

    sealed interface ProfileDataStates {
        data object Empty : ProfileDataStates
        data object Loading : ProfileDataStates
        data class Success(val userResponse: UserResponse) : ProfileDataStates
        data class Error(val error: String) : ProfileDataStates
    }
}

