package com.yeminnaing.chatapp.presentation.screens.authScreen

import android.widget.Toast
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.yeminnaing.chatapp.R
import com.yeminnaing.chatapp.presentation.navigation.Destination
import com.yeminnaing.chatapp.ui.theme.AppTheme


@Composable
fun SignInScreen() {
    val viewModel: AuthVm = hiltViewModel()
    val context = LocalContext.current
    val authState by viewModel.authStates.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            AuthVm.AuthStates.Authenticated -> {
                viewModel.login()
            }

            AuthVm.AuthStates.Empty -> {}
            is AuthVm.AuthStates.Error ->{
                Toast.makeText(context,((authState as AuthVm.AuthStates.Error).message) , Toast.LENGTH_SHORT).show()
            }
              is AuthVm.AuthStates.Loading ->{
                  Toast.makeText(context,"Loading" , Toast.LENGTH_SHORT).show()
              }
               is AuthVm.AuthStates.UnAuthenticated ->{}
        }

    }
    
    SignInScreenDesign(logIn = {email, password ->
        viewModel.logIn(email, password)
    }, navigation = {
        viewModel.navigateToRegisterScreen()
    })
}


@Composable
fun SignInScreenDesign(
    modifier: Modifier = Modifier,
    logIn: (email: String, password: String) -> Unit,
    navigation: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val emailFocusRequester = remember {
        FocusRequester()
    }
    val passwordRequester = remember {
        FocusRequester()
    }


    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    Box(modifier = modifier.fillMaxSize()
        .background(AppTheme.colorScheme.primary)
    ){
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
                        .padding(top = 12.dp)
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
                        }
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

                Button(onClick = {
                    logIn(email, password)
                }, modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Login")
                }

                Text(text = "If you don't have any account ", modifier.padding(top = 16.dp))
                TextButton(onClick = {
                    navigation()
                }) {
                    Text(text = "Register")
                }
            }
        }
    }



}


@Preview(showBackground = true)
@Composable
fun LogInScreenDesignPrev() {
    SignInScreenDesign(logIn = { email, password -> },
        navigation = {})
}