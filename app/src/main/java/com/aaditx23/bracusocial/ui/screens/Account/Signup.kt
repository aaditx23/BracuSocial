package com.aaditx23.bracusocial.ui.screens.Account

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aaditx23.bracusocial.backend.local.models.emptyProfileString
import com.aaditx23.bracusocial.backend.viewmodels.AccountVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Signup(
    email: String,
    name:String,
    accountvm: AccountVM,
    signupSuccess: () -> Unit
) {
    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }
    var isSuccess by rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val (id, setId) = rememberSaveable { mutableStateOf("") }
    val studentName by rememberSaveable { mutableStateOf(name) }
    val emailAddress by rememberSaveable { mutableStateOf(email) }
    val scope = rememberCoroutineScope()
    var signup by remember{
        mutableStateOf(false)
    }

    LaunchedEffect(signup) {
        if (signup) {
            println("Signup is true")
            if (id.isNotEmpty()) {
                println("id is not empty")
                try {
                    accountvm.createRemoteProfile(
                        listOf(
                            id,
                            "",
                            studentName,
                            "",
                            "",
                            "",
                            emptyProfileString,
                            emailAddress
                        )
                    )
                    accountvm.setLoginTrue()
                    println("Profile created successfully!")
                    signupSuccess()
                } catch (e: Exception) {
                    println("Error in creating profile: ${e.message}")
                    // Optionally show a Toast or error message here
                }
            } else {
                println("ID cannot be empty")
                // Optionally show a Toast here if ID is empty
            }
            // Reset the signup state to avoid re-triggering LaunchedEffect
            signup = false
        }
    }

    Column(
        modifier = Modifier
            .padding(top = 80.dp)
    ) {
        Text(
            text = "Welcome to BracuSocial!",
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold

        )
        Text(
            text = "Please enter your Student ID to create an account in BracuSocial",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium

        )
        TextField(
            value = emailAddress,
            onValueChange = {},
            label = { Text(text = "Email") },
            enabled = false,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),

            )
        TextField(
            value = id,
            onValueChange = {setId(it)},
            label = { Text(text = "ID") },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = { signup = true}
        ) {
            Text(text = "Signup")
        }

    }


}