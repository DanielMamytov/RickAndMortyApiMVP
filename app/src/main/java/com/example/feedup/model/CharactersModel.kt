package com.example.feedup.model

import kotlinx.serialization.Serializable
@Serializable
data class CharactersModel(
    val info: Info,
    val results: List<Result>
)