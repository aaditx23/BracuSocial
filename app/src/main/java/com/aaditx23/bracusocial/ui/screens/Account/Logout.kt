package com.aaditx23.bracusocial.ui.screens.Account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.viewmodels.AccountVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun Logout(
    accountvm: AccountVM,
    logoutSuccess:() ->  Unit
){
    var isSuccess by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            accountvm.deleteAllProfiles()
            isSuccess =  true
        }
    }
    if (isSuccess){
        logoutSuccess()
        isSuccess = false
    }

}