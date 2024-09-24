package com.yeminnaing.chatapp.presentation.authScreen

import android.widget.Toast
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.yeminnaing.chatapp.presentation.navigation.Screens

@Composable
fun RegisterScreen(navController: NavController ) {
    val viewModel: AuthVm = hiltViewModel()
    val context = LocalContext.current
    val authState by viewModel.authStates.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            AuthVm.AuthStates.Authenticated -> {
                navController.navigate(Screens.HomeScreen) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = false
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }

            AuthVm.AuthStates.Empty -> {}
            is AuthVm.AuthStates.Error ->{
                Toast.makeText(context,((authState as AuthVm.AuthStates.Error).message) , Toast.LENGTH_SHORT).show()
            }
            is  AuthVm.AuthStates.Loading->{
                Toast.makeText(context,"Loading" , Toast.LENGTH_SHORT).show()
            }
            is AuthVm.AuthStates.UnAuthenticated->{}
        }

    }

    RegisterScreenDesign(register = {name,email ,password ->
        viewModel.register(email=email, name = name, password = password)
    }, navigation = {
        navController.navigate(Screens.SignInScreen) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = false
                inclusive = true
            }
            launchSingleTop = true
        }
    })
}


@Composable
fun RegisterScreenDesign(
    modifier: Modifier = Modifier,
    register: (name: String, email: String, password: String) -> Unit,
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


    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var name by remember {
        mutableStateOf("")
    }
Box(modifier = modifier.fillMaxSize()){
    Card(modifier = modifier
        .align(Alignment.Center)
        .padding(16.dp)
        .clip(RoundedCornerShape(16.dp))

    ){
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


            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text(text = "Password") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                )
                ,modifier=modifier.padding(top = 10.dp)
            )

            Button(onClick = {
                register(name, email, password)
            },modifier.padding(top = 16.dp)) {
                Text(text = "Register")
            }

            Text(text = "If you already have an account ",modifier.padding(top = 16.dp))
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
    RegisterScreenDesign(register = {name, email, password ->  },

        navigation = {})
}