package com.aaditx23.bracusocial.application.Retrofit


import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

object RetrofitCookieJar : CookieJar {
    private val cookies = mutableListOf<Cookie>()
    val cookieMap: MutableMap<String, String> = mutableMapOf()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookies.filter { it.matches(url) }
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        this.cookies.addAll(cookies)
        cookies.filter { it.name == "authorization" }.forEach { cookie ->
            val value = "${cookie.name}=${cookie.value};${cookie.expiresAt};${cookie.domain}"
            cookieMap[cookie.name] = value
        }
    }

    fun clearCookies() {
        cookies.clear()
        cookieMap.clear()
        println("Cookies cleared.")
    }
}