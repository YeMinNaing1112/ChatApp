package com.yeminnaing.chatapp.presentation.screens.chatScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeminnaing.chatapp.domain.repositories.MessageRepo
import com.yeminnaing.chatapp.domain.responses.Message
import com.yeminnaing.chatapp.domain.responses.MessageResponse
import com.yeminnaing.chatapp.presentation.navigation.Destination
import com.yeminnaing.chatapp.presentation.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatScreenVm @Inject constructor(
    private val mMessageRepoImpl: MessageRepo,
    private val navigator: Navigator,
) : ViewModel() {
    private val _getMessageState = MutableStateFlow<GetMessageStates>(GetMessageStates.Empty)
    val getMessageStates = _getMessageState.asStateFlow()

    fun listenForMessage(chatId: String) {
        _getMessageState.value = GetMessageStates.Loading
        mMessageRepoImpl.listenForMessage(
            onSuccess = {
                _getMessageState.value = GetMessageStates.Success(it)
            },
            onFailure = {
                _getMessageState.value = GetMessageStates.Error(it)
            },
            chatId = chatId
        )
    }

    fun navigateBackToHome() {
        viewModelScope.launch {
            navigator.navigate(destination = Destination.HomeScreen,
                navOption = {
                    popUpTo(Destination.ChatScreen) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            )
        }
    }

    fun sendMessage(chatId: String, message: String) {
        mMessageRepoImpl.sendMessage(chatId, message)
    }



    sealed interface GetMessageStates {
        data object Empty : GetMessageStates
        data object Loading : GetMessageStates
        data class Success(val data: List<Message>) : GetMessageStates
        data class Error(val error: String) : GetMessageStates
    }
}
