package com.aaditx23.bracusocial.backend.remote.webview

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val login = "https://sso.bracu.ac.bd/realms/bracu/protocol/openid-connect/auth?client_id=slm&redirect_uri=https%3A%2F%2Fconnect.bracu.ac.bd%2F&"

@Composable
fun WebViewLogin(
    email: String,
    password: String,
    onTokenReceived: (String?) -> Unit,
    setLoading: (Boolean) -> Unit
) {
    var isCaptured by remember{
        mutableStateOf(false)
    }

        setLoading(true)
        AndroidView(
            modifier = Modifier
                .size(0.dp)
            ,
            factory = { context ->
                WebView(context).apply {
                    // Enable JavaScript
                    settings.javaScriptEnabled = true
                    settings.setSupportMultipleWindows(true)
                    settings.javaScriptCanOpenWindowsAutomatically = true

                    // Set up WebViewClient to handle redirects and page loads
                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(
                            view: WebView?,
                            url: String?,
                            favicon: android.graphics.Bitmap?
                        ) {
                            super.onPageStarted(view, url, favicon)
                            // Print each URL as it starts loading
                            println("Navigating to: $url")
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            // Print URL when page has finished loading
                            println("Finished loading: $url")

                            // Inject email and password into the fields and submit
                            if (url?.contains(login) == true) {
                                val loginScript = """
                                document.getElementById('username').value = '$email';
                                document.getElementById('password').value = '$password';
                                setTimeout(function() {
                                    const submitButton = document.querySelector('button[type="submit"], input[type="submit"], button.login-submit');
                                    if (submitButton) {
                                        submitButton.click();
                                    }
                                }, 100); // Delay to make sure the button is rendered
                            """
                                evaluateJavascript(loginScript) { result ->
                                    println("Form submitted with result: $result")
                                }
                            }
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: android.webkit.WebResourceError?
                        ) {
                            super.onReceivedError(view, request, error)
                            Toast.makeText(
                                context,
                                "Failed to load page: ${error?.description}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun shouldInterceptRequest(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): android.webkit.WebResourceResponse? {
                            val headers = request?.requestHeaders
                            val authorization = headers?.get("Authorization")
                            if (authorization != null) {
                                // If the Authorization header is found, send it to the callback

                                isCaptured = true

                                view?.post {
                                    view.stopLoading()
                                    view.clearHistory()  // Clears navigation history
                                    view.clearCache(true)  // Clears cache
                                    view.loadUrl("about:blank")  // Load a blank page instead of crashing
                                }
                                onTokenReceived(authorization)
                                setLoading(false)
                                println("Authorization Token: $authorization")
                            }
                            return super.shouldInterceptRequest(view, request)
                        }
                    }

                    // Load the initial URL
                    loadUrl("https://connect.bracu.ac.bd/")
                }
            },
            update = {
                // Any update logic (like sending cookies or headers) can go here later
            }
        )
}


