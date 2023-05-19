package com.example.components.api


import com.example.components.model.*
import com.example.components.utils.Constants
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiInterface {
    @GET("/dashboard")
    suspend fun getDashboardData(
        @Query("semester") semester: Int,
        @Query("Stream") Stream: String
    ): Response<DashboardData>

    @POST("/addsubjects")
    fun addSubjectData(@Body addSubjectDataModel: AddSubjectDataModel): Call<AddSubjectResponseModel>

    @GET("/student")
    fun getStudentDetails(@Query("student_id") student_id: String): Call<StudentDataResponse>

    @POST("/signup")
    fun signupStudent(@Body studentDataPostModel: StudentDataPostModel): Call<StudentSignupResponse>

    @POST("/login")
    fun login(@Body studentLoginPostModel: StudentLoginPostModel): Call<StudentDataResponse>

    @POST("/announcement")
    fun addAnnouncement(@Body announcementData: AnnouncementData): Call<AnnouncementResponse>

    @GET("/getAnnouncement")
    suspend  fun getAnnouncement(
        @Query("semester") semester: Int,
        @Query("Stream") Stream: String
    ):Response<GetAnnouncementResponse>

    @POST("/attendance")
    fun sendAttendance(@Body sendAttendanceList: SendAttendanceList):Call<AnnouncementResponse>

}

object ApiService {
    var apiInterface: ApiInterface

    init {
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory((GsonConverterFactory.create()))
            .build()

        apiInterface = retrofit.create(ApiInterface::class.java)
    }
}