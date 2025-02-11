package com.yeminnaing.chatapp.presentation.screens.profileScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yeminnaing.chatapp.R
import com.yeminnaing.chatapp.ui.theme.AppTheme

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    val viewModel: ProfileScreenVm = hiltViewModel()
    val profileState by viewModel.profileState.collectAsState()

    ProfileScreenDesign(profileState)
}

@Composable
fun ProfileScreenDesign(profileState: ProfileScreenVm.ProfileDataStates) {
    val context = LocalContext.current
    when (profileState) {
        is ProfileScreenVm.ProfileDataStates.Empty -> {

        }

        is ProfileScreenVm.ProfileDataStates.Error -> {
            Toast.makeText(context, profileState.error, Toast.LENGTH_SHORT).show()
        }

        is ProfileScreenVm.ProfileDataStates.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        is ProfileScreenVm.ProfileDataStates.Success -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier.weight(0.35f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = "ProfileBlurImage",
                            contentScale = ContentScale.Crop, modifier = Modifier
                                .fillMaxSize()
                                .blur(20.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(0.7f)
                            .fillMaxWidth()

                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(modifier = Modifier.weight(0.3f))
                Box(
                    modifier = Modifier
                        .weight(0.75f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(Color.White)
                )

            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.15f))

                ElevatedCard(
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 26.dp
                    ),
                    colors = CardDefaults.elevatedCardColors(
                        contentColor = Color.Transparent
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.defaultprofile),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .height(150.dp)
                            .width(150.dp), contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = profileState.userResponse.username,
                    color = AppTheme.colorScheme.secondary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = profileState.userResponse.email,
                    color = AppTheme.colorScheme.secondary,
                    fontSize = 16.sp,
                )

                Card(
                    modifier = Modifier
                        .padding(
                            16.dp
                        )
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .padding(vertical = 16.dp), horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Address",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = profileState.userResponse.address)

                    }
                }

                Card(
                    modifier = Modifier
                        .padding(
                            16.dp
                        )
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .padding(vertical = 16.dp), horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Bio",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = profileState.userResponse.bio)


                    }
                }


            }
        }
    }


}

@Preview
@Composable
private fun ProfileScreenDesignPrev() {
    ProfileScreenDesign(profileState = ProfileScreenVm.ProfileDataStates.Empty)
}