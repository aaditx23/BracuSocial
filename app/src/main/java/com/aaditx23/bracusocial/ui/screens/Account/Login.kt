package com.aaditx23.bracusocial.ui.screens.Account

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.local.models.Profile
import com.aaditx23.bracusocial.backend.remote.AccountProxyVM
import com.aaditx23.bracusocial.backend.viewmodels.AccountVM
import com.aaditx23.bracusocial.components.NoButtonDialog
import com.aaditx23.bracusocial.ui.theme.palette2DarkRed
import com.aaditx23.bracusocial.ui.theme.palette4green
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Login(accountvm: AccountVM, loginSuccess: () -> Unit){

    val ppVM: AccountProxyVM = hiltViewModel()
    var isSuccess by rememberSaveable {
        mutableStateOf(false)
    }
    var gotProfile by rememberSaveable {
        mutableStateOf(false)
    }
    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val (id, setId) = rememberSaveable { mutableStateOf("") }
    val (pass, setPass) = rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()



    if (isSuccess){
        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
        loginSuccess()
        isSuccess = false
    }
    else{
        Column(
            modifier = Modifier
                .padding(top = 80.dp)
        ) {
            TextField(
                value = id,
                onValueChange = {setId(it)},
                label = { Text(text = "ID") },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            TextField(
                value = pass,
                onValueChange = {setPass(it)},
                label = { Text(text = "Password") },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .border(2.dp, Color.Transparent),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Button(
                onClick = {
                    if(id == "" || pass == ""){
                        Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        scope.launch {
                            isLoading = true
                            ppVM.login(
                                id, pass,
                                result = {match, found ->
                                    isSuccess = match
                                    gotProfile = found

                                    if(!isSuccess){
                                        if(gotProfile){
                                            Toast.makeText(context, "Wrong Password", Toast.LENGTH_SHORT).show()
                                        }
                                        else{
                                            Toast.makeText(context, "Wrong ID", Toast.LENGTH_SHORT).show()
                                        }

                                    }
                                }
                            )
//                            if(isSuccess){
//                                accountvm.createFriends()
//                            }
                            isLoading = false

                        }
                    }
                }
            ) {
                Text(text = "Login")
            }
            if(isLoading){
                NoButtonDialog(title = "Logging in to $id", message = "Please wait...")
            }
        }
    }

}

