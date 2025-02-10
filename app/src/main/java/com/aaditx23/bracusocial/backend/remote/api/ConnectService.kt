package com.aaditx23.bracusocial.backend.remote.api

import com.aaditx23.bracusocial.backend.STUDENT_COURSES
import com.aaditx23.bracusocial.backend.STUDENT_IMAGE
import com.aaditx23.bracusocial.backend.STUDENT_INFO
import com.aaditx23.bracusocial.backend.remote.models.StudentCourse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface ConnectService{


    @GET("/")
    fun getLoginPage(): Call<ResponseBody>  // Step 1: Get redirected to login page

    @POST
    fun login(
        @Url url: String,  // Dynamic URL for login endpoint
        @Body requestBody: LoginRequest // Raw JSON payload
    ): Call<ResponseBody>

    @GET(STUDENT_INFO)
    fun getStudentInfo(
        @Header("Authorization") authorizationToken: String // Add Authorization header
    ): Call<List<StudentInfo>>

    @GET("$STUDENT_COURSES/{id}")
    fun getStudentCourses(
        @Path("id") id: String,  // Pass student ID dynamically in URL
        @Header("Authorization") authorizationToken: String  // Add Authorization header
    ): Call<List<StudentCourse>>

    @GET("$STUDENT_IMAGE/{fileName}")
    fun getStudentImage(
        @Path("fileName") fileName: String,
        @Header("Authorization") authorizationToken: String
    ): Call<ResponseBody>

}

data class LoginRequest(
    val username: String,
    val password: String
)

data class StudentInfo(
    val id: Int,
    val studentId: String,
    val fullName: String,
    val filePath: String?,
    val programOrCourse: String,
    val academicType: String,
    val cgpa: Double,
    val earnedCredit: Int,
    val attemptedCredit: Int,
    val currentSemester: String,
    val enrolledSemester: String,
    val departmentName: String,
    val studentEmail: String,
    val mobileNo: String
)

// course


