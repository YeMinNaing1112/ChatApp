package com.yeminnaing.chatapp.presentation.screens.profileScreen

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeminnaing.chatapp.domain.repositories.AuthRepo
import com.yeminnaing.chatapp.domain.repositories.ProfileRepo
import com.yeminnaing.chatapp.domain.responses.UserResponse
import com.yeminnaing.chatapp.presentation.navigation.Destination
import com.yeminnaing.chatapp.presentation.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenVm @Inject constructor(
    private val profileRepo: ProfileRepo,
    private val authRepo: AuthRepo,
    private val navigator: Navigator,
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
        profileRepo.editUserProfile(email, name, address, bio, image = "")
    }

    fun uploadImage(
        imageUri: Uri,
        user: UserResponse,
        context: Context,
    ) {
           profileRepo.uploadImage(imageUri, user, context)
    }

    fun signOut(){
        viewModelScope.launch {
            navigator.navigate(
                destination = Destination.AuthGraph,
                navOption = {
                    popUpTo(Destination.ProfileScreen) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            )
        }
         authRepo.signOut()
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

