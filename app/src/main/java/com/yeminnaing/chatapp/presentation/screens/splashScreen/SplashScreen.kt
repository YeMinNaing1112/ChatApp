package com.yeminnaing.chatapp.presentation.screens.splashScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.yeminnaing.chatapp.R
import com.yeminnaing.chatapp.presentation.screens.authScreen.AuthVm
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    val viewModel:SplashScreenVm = hiltViewModel()
    val authState by viewModel.authStates.collectAsState()
    val context= LocalContext.current
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

            is AuthVm.AuthStates.UnAuthenticated -> {
                viewModel.navigateToRegisterScreen()
            }
        }

    }
    SplashScreenDesign()
}

@Composable
fun SplashScreenDesign(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "SplashIcon",
            modifier = modifier.align(Alignment.Center)
        )

    }
}

@Preview
@Composable
fun SplashScreenVmPrev(modifier: Modifier = Modifier) {
    SplashScreenDesign()
}