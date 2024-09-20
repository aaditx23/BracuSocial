package com.aaditx23.bracusocial.ui.screens.Account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavHostController
import com.aaditx23.bracusocial.backend.local.models.emptyProfileString
import com.aaditx23.bracusocial.backend.viewmodels.AccountVM
import kotlinx.coroutines.launch
import androidx.compose.material3.Button as Button1

@Composable
fun Signup(
    email: String,
    name:String,
    accountvm: AccountVM,
    navController: NavHostController
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
    var signupFlag by remember{
        mutableStateOf(false)
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
            onClick = {
                println("Button clcicked")

                println(id)
                if (id.isNotEmpty()) {
                    println("id is not empty")
                    val profile = listOf(
                        id,
                        "",
                        studentName,
                        "",
                        "",
                        "",
                        "",
                        emailAddress
                    )

                    scope.launch {
                        accountvm.createProfile(
                            profileData = profile,
                            result = { r ->
                                if (r){
                                    navController.navigate("Profile")
                                }
                            }
                        )

                        println("Inside scope")
                    }
                }
            }
        ) {
            Text(text = "Signup")
        }

    }


}