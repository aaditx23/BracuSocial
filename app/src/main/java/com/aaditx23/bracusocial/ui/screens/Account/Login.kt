package com.aaditx23.bracusocial.ui.screens.Account

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.aaditx23.bracusocial.backend.remote.AccountProxyVM
import com.aaditx23.bracusocial.backend.remote.webview.WebViewLogin
import com.aaditx23.bracusocial.backend.viewmodels.AccountVM
import com.aaditx23.bracusocial.components.NoButtonCircularLoadingDialog
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream

@Composable
fun Login(
    accountvm: AccountVM,
//    loginButNoAccount: (name: String, email: String) -> Unit,
    navController: NavHostController
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
    var passwordVisible by remember { mutableStateOf(false) }
    var startWebView by remember { mutableStateOf(false) }
    var imageString by remember{
        mutableStateOf("")
    }

    val context = LocalContext.current
    val (email, setEmail) = rememberSaveable { mutableStateOf("") }
    var (pass, setPass) = rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()



    fun base64ToBitmap(base64String: String): Bitmap? {
        // Decode the Base64 string into a byte array
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)

        // Convert the byte array to an InputStream
        val byteArrayInputStream = ByteArrayInputStream(decodedString)

        // Decode the InputStream to a Bitmap
        return BitmapFactory.decodeStream(byteArrayInputStream)
    }

    Column(
        modifier = Modifier
            .padding(top = 80.dp)
    ) {
        if(imageString != ""){
            println("IMAGE FOUND")
            println(imageString)
            AsyncImage(
                model = base64ToBitmap(imageString),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(100.dp)
                    .border(1.dp, Color.Cyan),
                contentScale = ContentScale.FillWidth
            )
        }
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
            onValueChange = { setPass(it) },
            label = { Text(text = "USIS Password") },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .border(2.dp, Color.Transparent),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.VisibilityOff
                else Icons.Filled.Visibility

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            }
        )

        Button(
            onClick = {
                if(
                    email == "" || pass == ""
                    ){
                    Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
                else{
                    scope.launch {
                        println("Loggin in...")
                        startWebView = true
                    }
                }
            }
        ) {
            Text(text = "Login")
        }
        if(isLoading){
            NoButtonCircularLoadingDialog(title = "Logging in to $email", message = "Please wait...")
        }
        if(startWebView){
            WebViewLogin(
                email = email,
                password = pass,

                onTokenReceived = { token ->
                    if(token != null){
                        scope.launch {
                            accountvm.getStudentInfo(
                                authToken = token,
                                setImage = { image ->
                                    imageString = image
                                }
                            )
                        }
                    }
                    startWebView = false
                },
                setLoading = { loading ->
                    isLoading = loading
                }
            )
        }

    }


}

