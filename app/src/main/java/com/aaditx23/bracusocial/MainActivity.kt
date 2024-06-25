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

