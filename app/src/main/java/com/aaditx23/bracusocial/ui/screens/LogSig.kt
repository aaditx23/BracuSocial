package com.aaditx23.bracusocial.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.aaditx23.bracusocial.backend.viewmodels.AccountVM
import com.aaditx23.bracusocial.ui.screens.Account.Login

@Composable
fun Login_Signup(
    accountvm: AccountVM,
    success: () -> Unit,
    navController: NavHostController
){
    var toggle by rememberSaveable {
        mutableStateOf(false)
    }
    Column {
        Box {
            Login(
                accountvm = accountvm ,
                loginSuccess = success,
                loginButNoAccount = { name, email ->
                    navController.navigate("CreateAccount/${Uri.encode(email)}/${Uri.encode(name)}")
                }
            )
        }
//        Button(onClick = { toggle = !toggle }) {
//            Text(text = if (toggle) "Login" else "Signup")
//        }
    }
}