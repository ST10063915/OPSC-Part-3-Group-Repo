package com.opsc.onesecondapp


import com.opsc.onesecondapp.models.Entry
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("entries")
    fun createEntry(@Body entry: Entry): Call<Entry>

    @GET("entries")
    fun getEntries(@Query("userId") userId: String): Call<List<Entry>>

    @PUT("entries/{id}")
    fun updateEntry(@Path("id") entryId: String, @Body entry: Entry): Call<Entry>

    @DELETE("entries/{id}")
    fun deleteEntry(@Path("id") entryId: String): Call<Void>
}