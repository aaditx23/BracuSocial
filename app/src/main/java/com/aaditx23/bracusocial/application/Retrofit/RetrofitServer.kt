package com.aaditx23.bracusocial.application.Retrofit

import com.aaditx23.bracusocial.backend.CONNECT_BASE
import com.aaditx23.bracusocial.backend.remote.api.ConnectService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object RetrofitServer {

    lateinit var retrofit: Retrofit
    private lateinit var moshi: Moshi
    lateinit var Connect: ConnectService
        private set
    private val interceptor = Interceptor()
    fun initRetrofit() {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .cookieJar(RetrofitCookieJar)
            .followRedirects(true)
            .followSslRedirects(true)
            .addInterceptor(interceptor)
//            .addInterceptor(loggingInterceptor)       // use if need logs. else no.
            .build()

        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .baseUrl(CONNECT_BASE)
            .build()


        Connect = retrofit.create(ConnectService::class.java)

    }

}