package com.june0122.wakplus.data.api

import com.june0122.wakplus.data.entity.TwitchEventSubBody
import com.june0122.wakplus.data.entity.TwitchEventSubList
import retrofit2.Response
import retrofit2.http.*

interface TwitchEventSubService {
    @Headers("Content-Type: application/json")
    @POST("subscriptions")
    suspend fun postEventSub(@Body body: TwitchEventSubBody): TwitchEventSubList

    @GET("subscriptions")
    suspend fun getEventSubList(): TwitchEventSubList

    @DELETE("subscriptions")
    suspend fun deleteEventSub(@Query("id") userId: String): Response<Unit>
}