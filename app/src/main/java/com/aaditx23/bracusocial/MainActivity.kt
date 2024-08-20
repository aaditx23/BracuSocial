package com.aaditx23.bracusocial

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.aaditx23.bracusocial.ui.Main
import com.aaditx23.bracusocial.ui.theme.BracuSocialTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BracuSocialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {  innerPadding ->
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