package com.yeminnaing.chatapp.presentation.screens.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val chatsRepoImpl: ChatsRepo,
    private val mMessageRepoImpl: MessageRepo,
    private val navigator: Navigator,
) : ViewModel() {
    private val _getChatsStates = MutableStateFlow<GetChatsStates>(GetChatsStates.Empty)
    val getChatsStates = _getChatsStates.asStateFlow()
    private val _getLastMessageMap =
        MutableStateFlow<Map<String, GetLastMessage>>(emptyMap())
    val getLastMessageMap = _getLastMessageMap.asStateFlow()



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
        viewModelScope.launch {
            _getLastMessageMap.value = _getLastMessageMap.value.toMutableMap().apply {
                this[chatId] = GetLastMessage.Loading
            }

            mMessageRepoImpl.getLastMessage(
                onSuccess = { messageResponse ->
                    _getLastMessageMap.value = _getLastMessageMap.value.toMutableMap().apply {
                        this[chatId] = GetLastMessage.Success(messageResponse)
                    }
                },
                onFailure = { error ->
                    _getLastMessageMap.value = _getLastMessageMap.value.toMutableMap().apply {
                        this[chatId] = GetLastMessage.Error(error)
                    }
                },
                chatId = chatId
            )
        }
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