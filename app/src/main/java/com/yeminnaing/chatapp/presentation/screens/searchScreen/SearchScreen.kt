package com.yeminnaing.chatapp.presentation.screens.searchScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.yeminnaing.chatapp.ui.theme.AppTheme

@Composable
fun SearchScreen() {
    val viewmodel: SearchScreenVm = hiltViewModel()
    val searchState by viewmodel.searchState.collectAsState()

    SearchScreenDesign(searchStates = searchState, findUser = {user->
        if (user.endsWith("@gmail.com")){
            viewmodel.findByEmail(user)
        }else{
            viewmodel.findByName(user)
        }
    }, createChatIdandMessage = {
        viewmodel.createChatId(it)
//        viewmodel.createMessage()
    }, navigateChatScreen = {
        viewmodel.navigateToChatScreen()
    })

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenDesign(
    modifier: Modifier = Modifier,
    searchStates: SearchScreenVm.SearchStates,
    findUser: (String) -> Unit,
    createChatIdandMessage: (String) -> Unit,
    navigateChatScreen: () -> Unit,
) {
    var text by remember {
        mutableStateOf("")
    }
    var active by remember {
        mutableStateOf(false)
    }

   Column {
       SearchBar(
           modifier = modifier.fillMaxWidth(),
           query = text,
           onQueryChange = {
               text = it
           },
           onSearch = {
               val name = text.replace(" ", "")
               findUser(name)
               active = false

           },
           active = active,
           onActiveChange = {
               active = it
           }, placeholder = {
               Text(text = "Search")
           },
           leadingIcon = {
               Icon(
                   imageVector = Icons.Default.Search,
                   contentDescription = "Search"
               )
           },
           trailingIcon = {
               if (active) {
                   Icon(imageVector = Icons.Default.Close,
                       contentDescription = "Close", modifier = modifier.clickable {
                           text=""
                           active=false
                       }
                   )
               }
           }

       ) {


       }
       when (searchStates) {
           is SearchScreenVm.SearchStates.Empty -> {
//                    Text(text = "It Empty")
           }

           is SearchScreenVm.SearchStates.Error -> {
//                    Text(text = "Error")
           }

           is SearchScreenVm.SearchStates.Loading -> {
               CircularProgressIndicator()
           }

           is SearchScreenVm.SearchStates.Success -> {
               LazyColumn {
                   items(searchStates.users) { user->
                       Text(text = user.username ,
                           style = AppTheme.typography.body, color = AppTheme.colorScheme.secondary, modifier = Modifier
                               .fillMaxWidth()
                               .clickable {
                                   createChatIdandMessage(user.username)
                                   navigateChatScreen()
                               })
                   }
               }


           }

           else -> {}
       }
   }
}


@Preview(showBackground = true)
@Composable
private fun SearchScreenDesignPrev() {
    SearchScreenDesign(
        searchStates = SearchScreenVm.SearchStates.Empty,
        findUser = {},
        createChatIdandMessage = {},
        navigateChatScreen = {})
}