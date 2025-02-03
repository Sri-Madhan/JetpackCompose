package com.example.compose.networking

import de.jensklingenberg.ktorfit.http.GET

interface PoliceApiService {
    @GET("crimes-street-dates")
    suspend fun getStopAndSearchData(): List<StopAndSearchData>
}
