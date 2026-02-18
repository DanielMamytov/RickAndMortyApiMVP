package com.example.feedup.data.network

import com.example.feedup.model.TaskItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CharacterApi {

    @GET("task")
    fun getAllCharacters(): Call<List<TaskItem>>

    @POST("task")
    fun createTask(@Body task: TaskItem): Call<TaskItem>
}