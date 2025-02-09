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

val url = "https://sso.bracu.ac.bd/realms/bracu/protocol/openid-connect/auth?client_id=slm&redirect_uri=https%3A%2F%2Fconnect.bracu.ac.bd%2F&"

@Composable
fun WebViewLogin(
    email: String,
    password: String,
    onTokenReceived: (String?) -> Unit
) {
    val isLoading = remember { mutableStateOf(true) }

    // Creating WebView inside Compose
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                // Enable JavaScript
                settings.javaScriptEnabled = true
                settings.setSupportMultipleWindows(true)
                settings.javaScriptCanOpenWindowsAutomatically = true

                // Set up WebViewClient to handle redirects and page loads
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        // Print each URL as it starts loading
                        println("Navigating to: $url")
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        // Print URL when page has finished loading
                        println("Finished loading: $url")
                        isLoading.value = false

                        // Inject email and password into the fields and submit
                        if (url?.contains(url) == true) {
                            val loginScript = """
                                document.getElementById('username').value = '$email';
                                document.getElementById('password').value = '$password';
                                setTimeout(function() {
                                    // Try multiple ways to click the button
                                    const submitButton = document.querySelector('button[type="submit"], input[type="submit"], button.login-submit');
                                    if (submitButton) {
                                        console.log('Submit button found, clicking...');
                                        submitButton.click();
                                    } else {
                                        console.log('Submit button not found');
                                    }
                                }, 100); // Delay to make sure the button is rendered
                            """
                            evaluateJavascript(loginScript) { result ->
                                println("Form submitted with result: $result")
                            }
                        }
                    }

                    override fun onReceivedError(
                        view: WebView?, request: WebResourceRequest?, error: android.webkit.WebResourceError?
                    ) {
                        super.onReceivedError(view, request, error)
                        isLoading.value = false
                        Toast.makeText(context, "Failed to load page: ${error?.description}", Toast.LENGTH_SHORT).show()
                    }

                    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): android.webkit.WebResourceResponse? {
                        val headers = request?.requestHeaders
                        val authorization = headers?.get("Authorization")
                        if (authorization != null) {
                            // If the Authorization header is found, send it to the callback
                            onTokenReceived(authorization)
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

    // Show a loading indicator while the page is loading
    if (isLoading.value) {
        CircularProgressIndicator()
    }
}


