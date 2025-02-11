package com.yeminnaing.chatapp.presentation.screens.authScreen

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yeminnaing.chatapp.R
import com.yeminnaing.chatapp.ui.theme.AppTheme

@Composable
fun RegisterScreen() {
    val viewModel: AuthVm = hiltViewModel()
    val context = LocalContext.current
    val authState by viewModel.authStates.collectAsState()
    BackHandler {
        (context as? Activity)?.finish()
    }
    LaunchedEffect(authState) {
        when (authState) {
            AuthVm.AuthStates.Authenticated -> {
                viewModel.login()
            }

            AuthVm.AuthStates.Empty -> {}
            is AuthVm.AuthStates.Error -> {
                Toast.makeText(
                    context,
                    ((authState as AuthVm.AuthStates.Error).message),
                    Toast.LENGTH_SHORT
                ).show()
            }

            is AuthVm.AuthStates.Loading -> {
                Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
            }

            is AuthVm.AuthStates.UnAuthenticated -> {}
        }

    }

    RegisterScreenDesign(register = { name, email, password, address, bio ->
        viewModel.register(
            email = email,
            name = name,
            password = password,
            address = address,
            bio = bio
        )
    }, navigation = {
        viewModel.navigateToSignInScreen()
    })
}


@Composable
fun RegisterScreenDesign(
    modifier: Modifier = Modifier,
    register: (name: String, email: String, password: String, address: String, bio: String) -> Unit,
    navigation: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val emailFocusRequester = remember {
        FocusRequester()
    }
    val passwordRequester = remember {
        FocusRequester()
    }
    val nameRequester = remember {
        FocusRequester()
    }
    val addressRequester = remember {
        FocusRequester()
    }
    val bioRequester = remember {
        FocusRequester()
    }


    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var name by remember {
        mutableStateOf("")
    }
    var address by remember {
        mutableStateOf("")
    }
    var bio by remember {
        mutableStateOf("")
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colorScheme.primary)
    ) {
        Card(
            modifier = modifier
                .align(Alignment.Center)
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))

        ) {
            Column(
                modifier = modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text(text = "Name") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            emailFocusRequester.requestFocus()
                        }
                    ),
                    modifier = modifier
                        .focusRequester(nameRequester)
                        .padding(top = 10.dp)
                )

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text(text = "Email") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            passwordRequester.requestFocus()
                        }
                    ),
                    modifier = modifier
                        .focusRequester(emailFocusRequester)
                        .padding(top = 10.dp)
                )

                var visibility by remember {
                    mutableStateOf(false)
                }
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text(text = "Password") },
                    visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }, onNext = { addressRequester.requestFocus() }
                    ), modifier = modifier
                        .padding(top = 10.dp)
                        .focusRequester(passwordRequester),
                    trailingIcon = {
                        val image =
                            if (visibility) painterResource(id = R.drawable.eye_close)
                            else painterResource(id = R.drawable.eye_open)

                        Image(painter = image,
                            contentDescription = "password open/close icon",
                            Modifier.clickable {
                                visibility = !visibility
                            }
                        )
                    }
                )

                TextField(
                    value = address,
                    onValueChange = { address = it },
                    placeholder = { Text(text = "Address") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        },
                        onNext = { bioRequester.requestFocus() }
                    ), modifier = modifier
                        .padding(top = 10.dp)
                        .focusRequester(addressRequester)
                )
                TextField(
                    value = bio,
                    onValueChange = { bio = it },
                    placeholder = { Text(text = "Bio") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    ), modifier = modifier
                        .padding(top = 10.dp)
                        .focusRequester(bioRequester)
                )

                Button(
                    onClick = {
                        register(name, email, password, address, bio)
                    }, modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Register")
                }

                Text(
                    text = "If you already have an account ",
                    modifier
                        .padding(top = 16.dp)

                )
                TextButton(onClick = {
                    navigation()
                }) {
                    Text(text = "LoginHere")
                }
            }
        }
    }


}


@Preview
@Composable
private fun RegisterScreenDesignPrev() {
    RegisterScreenDesign(register = { name, email, password, address, bio -> },

        navigation = {})
}