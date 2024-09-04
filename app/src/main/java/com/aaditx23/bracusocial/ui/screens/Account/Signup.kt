package com.aaditx23.bracusocial.ui.screens.Account

import android.widget.Toast
import androidx.compose.foundation.border
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.aaditx23.bracusocial.backend.viewmodels.AccountVM
import com.aaditx23.bracusocial.ui.theme.palette2DarkRed
import com.aaditx23.bracusocial.ui.theme.palette4green
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Signup(
    accountvm: AccountVM,
    signupSuccess: () -> Unit
){
    val allProfile by accountvm.allProfiles.collectAsState()
    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }
    var isSuccess by rememberSaveable {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val (id, setId) = rememberSaveable { mutableStateOf("") }
    val (name, setName) = rememberSaveable { mutableStateOf("") }
    val (pass, setPass) = rememberSaveable { mutableStateOf("") }
    val (confirmPass, setConfirmPass) = rememberSaveable { mutableStateOf("") }

    LaunchedEffect(allProfile) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(200)
            isLoading = false
        }
    }

    if (isLoading){
//        NoButtonDialog(title = "Creating account: $id", message = "Please wait...")
        CircularProgressIndicator()
    }
    else if (isSuccess){

        Toast.makeText(context, "Signup Successful", Toast.LENGTH_SHORT).show()
        signupSuccess()
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
                value = name,
                onValueChange = {setName(it)},
                label = { Text(text = "Name") },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            )
            TextField(
                value = pass,
                onValueChange = {setPass(it)},
                label = { Text(text = "Password") },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        if (pass != confirmPass && pass != "" && confirmPass != "") palette2DarkRed
                        else if (pass == confirmPass && pass != "") palette4green
                        else Color.Transparent
                    ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            TextField(
                value = confirmPass,
                onValueChange = {setConfirmPass(it)},
                label = { Text(text = "Confirm Password") },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .border(
                        2.dp,
                        if (pass != confirmPass && pass != "" && confirmPass != "") palette2DarkRed
                        else if (pass == confirmPass && confirmPass != "") palette4green
                        else Color.Transparent
                    ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Button(onClick = { 
                if(id != "" &&
                    name != "" &&
                    pass != "" &&
                    pass == confirmPass
                    ){
                    
                    CoroutineScope(Dispatchers.IO).launch {
                        accountvm.createProfile(
                            listOf(
                                id,
                                pass,
                                name,
                                "",
                                "",
                                "",
                            ),
                            ifRepeat = { result ->

                                if (!result){
                                    Toast.makeText(context, "Profile already exists. Please Login.", Toast.LENGTH_SHORT).show()
                                }
                                isSuccess = result


                            }
                        )
                    }

                }
                else if (pass != confirmPass && pass != "" && confirmPass != ""){
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
                
            }) {
                Text(text = "Signup")
            }
            Button(onClick = {
                accountvm.deleteAllProfiles()
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
            }) {
                Text(text = "Delete All")
            }
            Text(text = allProfile.size.toString())
            
        }
    }

}