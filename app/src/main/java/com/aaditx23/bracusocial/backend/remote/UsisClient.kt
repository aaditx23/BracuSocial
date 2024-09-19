package com.aaditx23.bracusocial.backend.remote

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class USISClient {

    val session = "627124"

    // URL for login form
    private val loginUrl = "https://usis.bracu.ac.bd/academia/j_spring_security_check"
    // API URL to fetch the class schedule
//    private val scheduleUrl = "https://usis.bracu.ac.bd/academia/academicSection/listAcademicSectionWithSchedule?academiaSession=$session&_search=false&nd=1726233926089&rows=5000&page=1&sidx=course_code&sord=asc"
    val scheduleUrl = "https://usis.bracu.ac.bd/academia/studentCourse/advisedCourse"
    private val scheduleUrlClass = "https://usis.bracu.ac.bd/academia/studentCourse/showClassScheduleInTabularFormatInGrid?query=&academiaSession=$session&_search=false&nd=1726345445840&rows=5000&page=1&sidx=course_code&sord=asc"
    private val profileUrl = "https://usis.bracu.ac.bd/academia/student/showProfile?809&8090.20367876202457613&809"
    private val accountUrl = "https://usis.bracu.ac.bd/academia/systemUser/showMyAccounts?530"
    private val advisedCourses = "https://usis.bracu.ac.bd/academia/dashBoard/show#/academia/studentCourse/advisedCourse?582&5820.6471433739568049"


    suspend fun loginAndFetchName(email: String, pass: String): Pair<Boolean, String> {
        return suspendCancellableCoroutine { continuation ->
            val client = OkHttpClient.Builder()
                .cookieJar(object : CookieJar {
                    private val cookieStore = mutableMapOf<String, List<Cookie>>()

                    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                        cookieStore[url.host] = cookies
                    }

                    override fun loadForRequest(url: HttpUrl): List<Cookie> {
                        return cookieStore[url.host].orEmpty()
                    }
                })
                .build()

            // Step 1: Perform login using form data (not JSON)
            val formBody = FormBody.Builder()
                .add("j_username", email)
                .add("j_password", pass)
                .build()

            println("Credentials $email $pass")
            val loginRequest = Request.Builder()
                .url(loginUrl)  // Replace with your login URL
                .post(formBody)
                .build()

            client.newCall(loginRequest).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    continuation.resume(Pair(false, "No Name"))
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string().orEmpty()

                    if (response.isSuccessful && "Login" !in responseBody) {
                        println("Login successful!")
                        // Call fetchAccount and resume with its result
                        fetchAccount(client, continuation)
                    } else {
                        println("Login failed.")
                        continuation.resume(Pair(false, "No Name"))
                    }
                }
            })
        }
    }

    // Function to fetch the account and return the full name
    private fun fetchAccount(client: OkHttpClient, continuation: CancellableContinuation<Pair<Boolean, String>>) {
        val accountRequest = Request.Builder()
            .url(accountUrl)
            .build()

        client.newCall(accountRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
//                println("Failed to retrieve the account.")
                continuation.resume(Pair(false, "No Name"))
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val res = response.body?.string()
                    val document: Document = Jsoup.parse(res!!)
                    val fullName = document.select("tr:contains(Your Full Name) td").last()?.text()
                    if (!fullName.isNullOrEmpty()) {
//                        println("Your name is $fullName")
                        continuation.resume(Pair(true, fullName))
                    } else {
                        continuation.resume(Pair(false, "No Name"))
                    }
                } else {
//                    println("Failed to retrieve the account.")
                    continuation.resume(Pair(false, "No Name"))
                }
            }
        })
    }








}