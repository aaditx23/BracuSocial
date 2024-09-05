package com.aaditx23.bracusocial.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaditx23.bracusocial.ui.theme.palette7Green2
import com.aaditx23.bracusocial.ui.theme.paletteBlue5

@Composable
fun Pic_Name_ID(name: String, id: String, pic: String){
    val profileImage by remember { mutableStateOf(stringToBitmap(pic)!!) }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
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