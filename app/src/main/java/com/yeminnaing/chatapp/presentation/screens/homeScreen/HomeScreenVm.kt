package com.yeminnaing.chatapp.presentation.screens.homeScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeminnaing.chatapp.data.repositories.ChatsRepoImpl
import com.yeminnaing.chatapp.data.repositories.MessageRepoImpl
import com.yeminnaing.chatapp.domain.repositories.ChatsRepo
import com.yeminnaing.chatapp.domain.repositories.MessageRepo
import com.yeminnaing.chatapp.domain.responses.ChatResponse
import com.yeminnaing.chatapp.domain.responses.MessageResponse
import com.yeminnaing.chatapp.presentation.navigation.Destination
import com.yeminnaing.chatapp.presentation.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenVm @Inject constructor(
    private val chatsRepoImpl: ChatsRepoImpl,
    private val mMessageRepoImpl: MessageRepoImpl,
    private val navigator: Navigator,
) : ViewModel() {
    private val _getChatsStates = MutableStateFlow<GetChatsStates>(GetChatsStates.Empty)
    val getChatsStates = _getChatsStates.asStateFlow()

    private val _getLastMessage = MutableStateFlow<GetLastMessage>(GetLastMessage.Empty)
    val getLastMessage = _getLastMessage.asStateFlow()

    init {
        getChats()
    }



    fun navigateToChatScreen(id: String) {
        viewModelScope.launch {
            navigator.navigate(destination = Destination.ChatScreen(id = id),
                navOption = {
                    popUpTo(Destination.HomeScreen) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            )
        }

    }

    fun navigateToSearchScreen() {
        viewModelScope.launch {
            navigator.navigate(destination = Destination.SearchScreen,
                navOption = {
                    popUpTo(Destination.HomeScreen) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            )
        }
    }


    private fun getChats() {
        _getChatsStates.value = GetChatsStates.Loading
        chatsRepoImpl.getChats(
            onSuccess = {
                _getChatsStates.value = GetChatsStates.Success(it)
            },
            onFailure = {
                _getChatsStates.value = GetChatsStates.Error(it)
            }
        )
    }

    fun upDateChatWithLastMessage(chatId: String) {
        mMessageRepoImpl.getLastMessage(
            onSuccess = {
                _getLastMessage.value = GetLastMessage.Success(it)
            },
            onFailure = {
                _getLastMessage.value = GetLastMessage.Error(it)
            },
            chatId = chatId
        )

    }

    sealed interface GetChatsStates {
        data object Empty : GetChatsStates
        data object Loading : GetChatsStates
        data class Success(val data: List<ChatResponse>) : GetChatsStates
        data class Error(val error: String) : GetChatsStates
    }

    sealed interface GetLastMessage {
        data object Empty : GetLastMessage
        data object Loading : GetLastMessage
        data class Success(val message: MessageResponse) : GetLastMessage
        data class Error(val error: String) : GetLastMessage
    }

}