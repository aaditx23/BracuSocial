package com.aaditx23.bracusocial.application.Retrofit

import okhttp3.Interceptor
import okhttp3.Response

class Interceptor : Interceptor {
    var authToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Proceed with the request
        val response = chain.proceed(request)

        // Check if the Authorization token is present in the response headers
        val token = request.header("Authorization")
        if (token != null) {
            // The token is in the "Authorization" header of the response
            authToken = token.split(" ")[1]  // Split to get the token without 'Bearer'
        }

        // Log the token (for debugging purposes)
        println("✅ Authorization Token: $authToken")

        return response
    }
}
