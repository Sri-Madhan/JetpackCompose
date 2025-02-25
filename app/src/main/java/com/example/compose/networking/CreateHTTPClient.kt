package com.example.compose.networking

//import androidx.privacysandbox.tools.core.generator.build
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.*
//import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*;

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object KtorfitInstance {
     val client = HttpClient() {
        install(ContentNegotiation) {
            json(
                json = Json{
                    ignoreUnknownKeys =true
                }
            )
        }
         install(HttpTimeout) {
             requestTimeoutMillis = 15000
             connectTimeoutMillis = 10000
             socketTimeoutMillis = 10000
         }
    }

    private val ktorfit = Ktorfit.Builder()
        .httpClient(client)
        .baseUrl("https://data.police.uk/api/")
        .build()

//    fun createPoliceApiService(): PoliceApiService {
//        return ktorfit.create<PoliceApiService>()
//    }

//    inline fun <reified T> createApiService(): T {
//        return Ktorfit.Builder()
//            .httpClient(client)
//            .baseUrl("https://data.police.uk/api/")
//            .build()
//            .create<T>()
//    }

//    fun createApiService(): Ktorfit {
//        return Ktorfit.Builder()
//            .httpClient(client)
//            .baseUrl("https://data.police.uk/api/")
//            .build()
//    }




}
