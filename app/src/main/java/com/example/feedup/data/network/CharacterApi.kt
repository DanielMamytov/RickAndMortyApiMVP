package com.example.feedup.data.network

import com.example.feedup.model.TaskItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CharacterApi {

    @GET("task")
    fun getAllCharacters(): Call<List<TaskItem>>

    @GET("task/{id}")
    fun getTaskById(@Path("id") id: String): Call<TaskItem>

    @POST("task")
    fun createTask(@Body task: TaskItem): Call<TaskItem>

    @PUT("task/{id}")
    fun updateTask(@Path("id") id: String, @Body task: TaskItem): Call<TaskItem>

    @PATCH("task/{id}")
    fun patchTask(@Path("id") id: String, task: Map<String, String>): Call<TaskItem>

    @DELETE("task/{id}")
    fun deleteTask(@Path("id") id: String): Call<Unit>
}