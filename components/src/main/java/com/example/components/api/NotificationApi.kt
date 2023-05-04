package com.example.components.api

import com.example.components.model.PushNotificationModel
import com.example.components.utils.Constants
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {
    @Headers("Authorization: key=${Constants.FIREBASE_SERVER_KEY}","Coontent-Type:${Constants.CONTENT_TYPE}")
    @POST("/fcm/send")
    suspend fun postNotification(@Body notification: PushNotificationModel): Response<ResponseBody>
}
object NotificationApiService{
    var notificationAPi: NotificationApi
    init{
        val retrofit = Retrofit.Builder().baseUrl(Constants.FIREBASE_BASE_URL).addConverterFactory((GsonConverterFactory.create()))
            .build()

        notificationAPi = retrofit.create(NotificationApi::class.java)
    }
}