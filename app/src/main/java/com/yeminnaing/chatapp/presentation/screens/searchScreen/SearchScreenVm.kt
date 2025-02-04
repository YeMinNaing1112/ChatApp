package com.yeminnaing.chatapp.presentation.screens.searchScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.yeminnaing.chatapp.domain.repositories.ChatsRepo
import com.yeminnaing.chatapp.domain.repositories.MessageRepo
import com.yeminnaing.chatapp.domain.responses.UserResponse
import com.yeminnaing.chatapp.presentation.navigation.Destination
import com.yeminnaing.chatapp.presentation.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchScreenVm @Inject constructor(
    private val chatsRepoImpl: ChatsRepo,
    private val messageRepoImpl: MessageRepo,
    private val auth: FirebaseAuth,
    private val navigator: Navigator,
) : ViewModel() {
    private val _searchState = MutableStateFlow<SearchStates>(SearchStates.Empty)
    val searchState = _searchState.asStateFlow()

    lateinit var chatId: String

    fun navigateToChatScreen() {
        viewModelScope.launch {
            navigator.navigate(destination = Destination.ChatScreen(id = chatId),
                navOption = {
                    popUpTo(Destination.SearchScreen) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            )
        }
    }


    fun createChatId(targetUser: String) {
        val currentUserId = auth.currentUser?.displayName ?: ""
        chatId = generateChatId(currentUserId, targetUser)
        chatsRepoImpl.createChat(chatId, targetUser)
    }

    fun createMessage() {
          messageRepoImpl.createMessage(chatId)
    }


    fun findByEmail(email: String) {
        _searchState.value = SearchStates.Loading
        chatsRepoImpl.findUserByEmail(
            email = email,
            onSuccess = {
                _searchState.value = SearchStates.Success(it)
            },
            onFailure = {
                _searchState.value = SearchStates.Error(it)
            }
        )

    }


    private fun generateChatId(currentUserId: String, targetUserId: String): String {
        val sortedIds = listOf(currentUserId, targetUserId).sorted()
        return sortedIds.joinToString("_")
    }

    sealed interface SearchStates {
        data object Empty : SearchStates
        data object Loading : SearchStates
        data class Success(val users: UserResponse) : SearchStates
        data class Error(val error: String) : SearchStates
    }
}