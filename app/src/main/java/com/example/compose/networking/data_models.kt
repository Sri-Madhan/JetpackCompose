package com.example.compose.networking

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StopAndSearchData(
    val date: String,

    @SerialName("stop-and-search")
    val stopAndSearch: List<String>
)

@Serializable
data class StopAndSearchResponse(
    val data: List<StopAndSearchData>
)