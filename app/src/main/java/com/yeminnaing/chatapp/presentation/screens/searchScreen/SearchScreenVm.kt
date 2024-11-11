package com.yeminnaing.chatapp.presentation.screens.searchScreen

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.yeminnaing.chatapp.data.repositories.ChatsRepoImpl
import com.yeminnaing.chatapp.domain.responses.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchScreenVm @Inject constructor(
    private val chatsRepoImpl: ChatsRepoImpl,
    private val auth: FirebaseAuth,
) : ViewModel() {
    private val _searchState = MutableStateFlow<SearchStates>(SearchStates.Empty)
    val searchState = _searchState.asStateFlow()

    lateinit var chatId: String


//    private val _searchText= MutableStateFlow("")
//    private val searchText=_searchText.asStateFlow()
//
//    fun onSearchTextChange(text:String){
//        _searchText.value= text
//    }


//    init {
//        viewModelScope.launch {
//            searchText.debounce(300)
//                .distinctUntilChanged()
//                .collect{ query->
//
//                }
//        }
//    }


    fun createChatId(targetUser: String) {
        val currentUserId = auth.currentUser?.displayName ?: ""
        chatId = generateChatId(currentUserId,targetUser)
        chatsRepoImpl.createChat(chatId, targetUser)
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