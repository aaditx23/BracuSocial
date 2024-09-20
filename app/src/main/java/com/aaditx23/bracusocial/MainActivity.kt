package com.aaditx23.bracusocial

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.aaditx23.bracusocial.components.drawableToBitmap
import com.aaditx23.bracusocial.ui.Main
import com.aaditx23.bracusocial.ui.theme.BracuSocialTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    object EmptyImage{
        lateinit var emptyProfileImage: Bitmap
    }
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val drawableEmptyProfile = this.getDrawable(R.drawable.baseline_person_24)
        lifecycleScope.launch(Dispatchers.IO) {
            // Perform the bitmap conversion in the background
            EmptyImage.emptyProfileImage = drawableToBitmap(drawableEmptyProfile!!)

        }
        setContent {
            BracuSocialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Main()
                }
            }
        }
    }
}

// features

//1. Login/Signup
//2. Save course data in phone (offline persistent)
//3. Browse courses
//4. Pre advising feature for arranging combinations
//5. Save demo routines
//6. Save user's current routine
//7. Add friends
//6. Find friends
//7. Friend requests
//8. See own routine
//9. See friends' routine all in one place
//10. Find free friends
//11. Reset password
//12. Search courses
//13. Search users