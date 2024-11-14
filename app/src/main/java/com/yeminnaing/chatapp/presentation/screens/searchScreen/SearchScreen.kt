package com.yeminnaing.chatapp.presentation.screens.searchScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.yeminnaing.chatapp.R

@Composable
fun SearchScreen() {
    val viewmodel: SearchScreenVm = hiltViewModel()
    val searchState by viewmodel.searchState.collectAsState()

    SearchScreenDesign(searchStates = searchState, findByEmail = {
        viewmodel.findByEmail(it)
    }, createChatId = {
        viewmodel.createChatId(it)
    }, navigateChatScreen = {
        viewmodel.navigateToChatScreen()
    })

}


@Composable
fun SearchScreenDesign(
    modifier: Modifier = Modifier,
    searchStates: SearchScreenVm.SearchStates,
    findByEmail: (String) -> Unit,
    createChatId: (String) -> Unit,
    navigateChatScreen: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchRequestFocus = remember {
        FocusRequester()
    }

    var search by remember {
        mutableStateOf(TextFieldValue(""))
    }

    Scaffold {
        Column {
            //region SearchBar

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(it)
                    .height(56.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                    contentDescription = "Back",
                    modifier = modifier.padding(start = 16.dp, top = 10.dp)
                )

                BasicTextField(
                    value = search,
                    onValueChange = {
                        search = it
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email, imeAction = ImeAction.Search
                    ),
                    textStyle = TextStyle(fontSize = 24.sp, color = Color.Black),
                    cursorBrush = SolidColor(Color.Black.copy(alpha = 1f)), // Change cursor color
                    decorationBox = { innerText ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart // Center the text and cursor
                        ) {
                            if (search.text.isEmpty()) {
                                Text(
                                    text = "Search",
                                    color = Color.Gray,
                                    style = TextStyle(fontSize = 24.sp),

                                    )

                            }
                        }

                        innerText()
                    },

                    keyboardActions = KeyboardActions(onSearch = {
                        keyboardController?.hide()
                        val email = search.text.replace(" ", "")
                        findByEmail(email)
                    }),
                    modifier = modifier
                        .height(40.dp)
                        .focusRequester(searchRequestFocus)
                        .align(Alignment.CenterVertically)

                )
            }
            //endregion

            //region SearchList

            when (searchStates) {
                is SearchScreenVm.SearchStates.Empty -> {
                    Text(text = "It Empty")
                }

                is SearchScreenVm.SearchStates.Error -> {
                    Text(text = "Error")
                }

                is SearchScreenVm.SearchStates.Loading -> {
                    CircularProgressIndicator()
                }

                is SearchScreenVm.SearchStates.Success -> {
                    Text(text = searchStates.users.username, modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            createChatId(searchStates.users.username)
                            navigateChatScreen()
                        })
                }

                else -> {}
            }
            //endregion
        }

    }


}


@Preview(showBackground = true)
@Composable
private fun SearchScreenDesignPrev() {
    SearchScreenDesign(
        searchStates = SearchScreenVm.SearchStates.Empty,
        findByEmail = {},
        createChatId = {},
        navigateChatScreen = {})
}