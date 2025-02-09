package com.aaditx23.bracusocial.backend.remote.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface ConnectService{


    @GET("/")
    fun getLoginPage(): Call<ResponseBody>  // Step 1: Get redirected to login page

    @POST
    fun login(
        @Url url: String,  // Dynamic URL for login endpoint
        @Body requestBody: LoginRequest // Raw JSON payload
    ): Call<ResponseBody>

    @GET("api/mds/v1/portfolios")
    fun getStudentInfo(
        @Header("Authorization") authorizationToken: String // Add Authorization header
    ): Call<ResponseBody>

}

data class LoginRequest(
    val username: String,
    val password: String
)