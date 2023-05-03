package com.example.components.api


import com.example.components.model.DashboardData
import com.example.components.utils.Constants
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {
    @GET("/dashboard")
    suspend fun getDashboardData(@Query("semester") semester:Int,@Query("Stream") Stream:String):Response<DashboardData>



}
object ApiService{
     var apiInterface: ApiInterface
    init{
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory((GsonConverterFactory.create()))
            .build()

        apiInterface = retrofit.create(ApiInterface::class.java)
    }
}