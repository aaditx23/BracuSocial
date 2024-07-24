package com.aaditx23.bracusocial.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.aaditx23.bracusocial.backend.viewmodels.AccountVM
import com.aaditx23.bracusocial.ui.screens.Account.Login
import com.aaditx23.bracusocial.ui.screens.Account.Signup

@Composable
fun Login_Signup(
    accountvm: AccountVM,
    success: () -> Unit
){
    var toggle by rememberSaveable {
        mutableStateOf(false)
    }
    Column {
        Box {
            if (toggle){
                Signup(
                    accountvm = accountvm,
                    signupSuccess = success
                )
            }
            else{
                Login(
                    accountvm = accountvm ,
                    loginSuccess = success
                )
            }
        }
        Button(onClick = { toggle =! toggle }) {
            Text(text = if (toggle) "Login" else "Signup")
        }
    }
}