package com.yeminnaing.chatapp.presentation.screens.profileScreen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.yeminnaing.chatapp.R
import com.yeminnaing.chatapp.domain.responses.UserResponse
import com.yeminnaing.chatapp.ui.theme.AppTheme

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    val viewModel: ProfileScreenVm = hiltViewModel()
    val profileState by viewModel.profileState.collectAsState()
    val context = LocalContext.current
    ProfileScreenDesign(profileState, editUserProfile = { name, address, bio, email ->
        viewModel.editProfile(name = name, address = address, email = email, bio = bio)
    }, upLoadPhoto = { imageUri, userResponse ->
        viewModel.uploadImage(imageUri, userResponse, context)
    }, signOut = {
        viewModel.signOut()
    })
}

@Composable
fun ProfileScreenDesign(
    profileState: ProfileScreenVm.ProfileDataStates,
    editUserProfile: (name: String, address: String, bio: String, email: String) -> Unit,
    upLoadPhoto: (imageUri: Uri, userResponse: UserResponse) -> Unit,
    signOut:()->Unit
) {
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
        }
    )
    val context = LocalContext.current
    var dialogFlag by remember {
        mutableStateOf(false)
    }

    var upLoadImageDialogFlag by remember {
        mutableStateOf(false)
    }
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
                            painter = painterResource(id = R.drawable.defaultprofile),
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

                IconButton(
                    onClick = { }, modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 32.dp, end = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_photo_camera_24),
                        contentDescription = "Add Profile Photo", modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.LightGray.copy(0.8f))
                            .padding(5.dp)


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
                        .background(AppTheme.colorScheme.primary)
                ) {
                    IconButton(
                        onClick = {
                            dialogFlag = !dialogFlag
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 16.dp, end = 16.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Profile")

                    }
                    Button(
                        onClick = {
                            signOut()
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 16.dp, end = 16.dp)
                    ) {
                        Text(text = "Sign Out")

                    }
                }

            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.15f))
                Box(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .size(150.dp)
                ) {
                    var selectedImageUri by remember {
                        mutableStateOf<Uri?>(null)
                    }
                    val photoPickerLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.PickVisualMedia(),
                        onResult = { uri ->
                            selectedImageUri = uri
                        }
                    )
                    ElevatedCard(
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 26.dp
                        ),
                        colors = CardDefaults.elevatedCardColors(
                            contentColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                    ) {

                        if (profileState.userResponse.image == "") {
                            Image(
                                painter = painterResource(id = R.drawable.defaultprofile),
                                contentDescription = "Profile",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.White, CircleShape)
                            )
                        } else {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Profile",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.White, CircleShape)
                            )
                        }

                    }
                    IconButton(onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                        upLoadImageDialogFlag = true

                    }, modifier = Modifier.align(Alignment.BottomEnd)) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_photo_camera_24),
                            contentDescription = "Add Profile Photo", modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.LightGray.copy(0.8f))
                                .padding(5.dp)


                        )
                    }

                    if (upLoadImageDialogFlag) {
                        Dialog(onDismissRequest = { upLoadImageDialogFlag = false }) {
                            selectedImageUri?.let {
                                UploadPhotoDialog(uri = it) {
                                    upLoadPhoto(it, profileState.userResponse)
                                }
                            }
                        }
                    }
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




                if (dialogFlag) {
                    Dialog(onDismissRequest = { dialogFlag = false }) {
                        ProfileDialog(userResponse = profileState.userResponse) { name, address, bio ->
                            editUserProfile(name, address, bio, profileState.userResponse.email)
                            dialogFlag = !dialogFlag
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun UploadPhotoDialog(uri: Uri, upload: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = uri, contentDescription = "SelectedPhoto",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
            )
            Text(
                text = "Are You Confirm to Change the Image",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 32.dp)
            )
            Button(onClick = { upload() }) {
                Text(text = "Save")
            }
        }
    }
}


@Composable
fun ProfileDialog(
    userResponse: UserResponse,
    editUserProfile: (name: String, address: String, bio: String) -> Unit,
) {
    val nameRequester = remember {
        FocusRequester()
    }
    val addressRequester = remember {
        FocusRequester()
    }
    val bioRequester = remember {
        FocusRequester()
    }


    var name by remember {
        mutableStateOf(userResponse.username)
    }

    var address by remember {
        mutableStateOf(userResponse.address)
    }

    var bio by remember {
        mutableStateOf(userResponse.bio)
    }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Name",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp)
            )
            TextField(
                value = name,
                onValueChange = { name = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        addressRequester.requestFocus()
                    }
                ),
                modifier = Modifier
                    .focusRequester(nameRequester)
            )

            Text(
                text = "Address",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp)
            )
            TextField(
                value = address,
                onValueChange = { address = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        bioRequester.requestFocus()
                    }
                ),
                modifier = Modifier
                    .focusRequester(addressRequester)
            )
            Text(
                text = "Bio",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp)
            )
            TextField(
                value = bio,
                onValueChange = { bio = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        ImeAction.Done
                    }
                ),
                modifier = Modifier
                    .focusRequester(bioRequester)
            )


            Button(
                onClick = {
                    editUserProfile(name, address, bio)
                }, Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Edit")
            }

        }
    }
}


@Preview
@Composable
private fun ProfileScreenDesignPrev() {
    ProfileScreenDesign(
        profileState = ProfileScreenVm.ProfileDataStates.Success(
            userResponse = UserResponse(
                userId = "1",
                username = "Aung Aung", email = "test@email.com",
                address = "Taungoo",
                bio = "This is a Biography"
            )
        ), editUserProfile = { _, _, _, _ -> },
        upLoadPhoto = { _, _ -> },
        signOut = {}
    )
}

@Preview
@Composable
private fun ProfileDialogPrev() {
    ProfileDialog(
        userResponse = UserResponse(
            userId = "1",
            username = "Aung Aung",
            email = "test@email.com",
            address = "Taungoo",
            bio = "This is a Biography"
        ), editUserProfile = { _, _, _ -> }
    )
}

@Preview
@Composable
private fun UpLoadPhotoDialogPrev() {
    UploadPhotoDialog(uri = Uri.EMPTY) {

    }
}