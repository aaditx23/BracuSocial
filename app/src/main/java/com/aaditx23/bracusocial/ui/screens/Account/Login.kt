package com.aaditx23.bracusocial.ui.screens.Account

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aaditx23.bracusocial.backend.remote.AccountProxyVM
import com.aaditx23.bracusocial.backend.viewmodels.AccountVM
import com.aaditx23.bracusocial.components.NoButtonCircularLoadingDialog
import kotlinx.coroutines.launch

@Composable
fun Login(
    accountvm: AccountVM,
    loginSuccess: () -> Unit,
    loginButNoAccount: (name: String, email: String) -> Unit
){

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
    val (email, setEmail) = rememberSaveable { mutableStateOf("") }
    val (pass, setPass) = rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()




    Column(
        modifier = Modifier
            .padding(top = 80.dp)
    ) {
        TextField(
            value = email,
            onValueChange = {setEmail(it)},
            label = { Text(text = "USIS Email") },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        TextField(
            value = pass,
            onValueChange = {setPass(it)},
            label = { Text(text = "USIS Password") },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .border(2.dp, Color.Transparent),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                if(email == "" || pass == ""){
                    Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
                else{
                    scope.launch {
                        isLoading = true
                        println("Loggin in...")
                        ppVM.login(
                            email, pass,
                            result = { login, name, gotProfile ->

                                if(login){
                                    if(gotProfile){
                                        Toast.makeText(
                                            context,
                                            "Login Successful",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        loginSuccess()
                                    }
                                    else{
                                        loginButNoAccount(name, email)
                                    }
                                }
                                else{
                                    Toast.makeText(
                                        context,
                                        "Wrong USIS Credentials",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                        isLoading = false

                    }
                }
            }
        ) {
            Text(text = "Login")
        }
        if(isLoading){
            NoButtonCircularLoadingDialog(title = "Logging in to $email", message = "Please wait...")
        }
    }


}

