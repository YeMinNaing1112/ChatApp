package com.yeminnaing.chatapp.presentation.screens.profileScreen

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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeminnaing.chatapp.R
import com.yeminnaing.chatapp.ui.theme.AppTheme

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {

}

@Composable
fun ProfileScreenDesign() {
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
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
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
            text = "Ye Min Naing",
            color = AppTheme.colorScheme.secondary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "yeminnaing11112@gmail.com",
            color = AppTheme.colorScheme.secondary,
            fontSize = 16.sp,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp), horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Address",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Taungoo")
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Bio",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Steven Paul Jobs was an American businessman, inventor, and investor best known for co-founding the technology company Apple Inc. Jobs was also the founder of NeXT and chairman and majority shareholder of Pixa")
        }

    }


}

@Preview
@Composable
private fun ProfileScreenDesignPrev() {
    ProfileScreenDesign()
}