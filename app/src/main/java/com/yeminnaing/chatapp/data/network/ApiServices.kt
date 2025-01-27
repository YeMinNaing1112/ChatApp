package com.yeminnaing.chatapp.data.network

import com.yeminnaing.chatapp.data.responses.FcmRequestBody
import com.yeminnaing.chatapp.data.responses.FcmResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiServices {
    @Headers("Content-Type: application/json")
    @POST("v1/projects/chatapp-60ab0/messages:send")
     fun sendNotification(
        @Body payload: FcmRequestBody
    ): Call<FcmResponse>
}