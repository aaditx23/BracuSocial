package com.aaditx23.bracusocial.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaditx23.bracusocial.MainActivity
import com.aaditx23.bracusocial.ui.theme.palette7Green2
import com.aaditx23.bracusocial.ui.theme.paletteBlue1
import com.aaditx23.bracusocial.ui.theme.paletteBlue5
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Pic_Name_ID(name: String, id: String, pic: String){
    val scope = rememberCoroutineScope()
    var profileImage by remember { mutableStateOf(MainActivity.EmptyImage.emptyProfileImage) }
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        scope.launch {
            profileImage =  withContext(Dispatchers.IO){
                stringToBitmap(pic)!!
            }
            isLoading = false
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        if(isLoading){
            Box(
                modifier = Modifier
                    .size(50.dp),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp),
                    color = paletteBlue1
                )
            }
        }
        else{
            Image(
                painter = BitmapPainter(image = profileImage.asImageBitmap()),
                contentDescription = name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(
                        1.dp,
                        color = paletteBlue5,
                        shape = CircleShape
                    ),
                contentScale = ContentScale.FillWidth,
            )
        }
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                text = name,
                color = palette7Green2,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = id,
                color = palette7Green2,
                fontSize = 15.sp
            )
        }
    }
}