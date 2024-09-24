package com.yeminnaing.chatapp.presentation.chatScreen

import androidx.lifecycle.ViewModel
import com.yeminnaing.chatapp.data.repositories.MessageRepoImpl
import com.yeminnaing.chatapp.domain.responses.MessageResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatScreenVm @Inject constructor(
    private val mMessageRepoImpl: MessageRepoImpl,
) : ViewModel() {
    private val _getMessageState = MutableStateFlow<GetMessageStates>(GetMessageStates.Empty)
    val getMessageStates = _getMessageState.asStateFlow()


    fun listenForMessage(channelId: String) {
        _getMessageState.value = GetMessageStates.Loading
        mMessageRepoImpl.listenForMessage(
            onSuccess = {
                _getMessageState.value = GetMessageStates.Success(it)
            },
            onFailure = {
                _getMessageState.value = GetMessageStates.Error(it)
            },
            channelId = channelId
        )
    }


    fun sendMessage(channelId: String, message: String) {
        mMessageRepoImpl.sendMessage(channelId, message)
    }

    sealed interface GetMessageStates {
        data object Empty : GetMessageStates
        data object Loading : GetMessageStates
        data class Success(val data: List<MessageResponse>) : GetMessageStates
        data class Error(val error: String) : GetMessageStates
    }

}