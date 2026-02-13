package com.example.feedup.data.network

import com.example.feedup.model.CharactersModel
import retrofit2.Call
import retrofit2.http.GET

interface CharacterApi {

    @GET("character")
    fun getAllCharacters(): Call<CharactersModel>
}