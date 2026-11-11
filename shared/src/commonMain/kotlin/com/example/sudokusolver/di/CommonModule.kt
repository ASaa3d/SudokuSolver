package com.example.sudokusolver.di

import com.example.sudokusolver.BuildKonfig
import core.ApiError
import core.ApiException
import com.example.sudokusolver.auth.di.authModule
import com.example.sudokusolver.splash.di.splashModule
import com.example.sudokusolver.sudoku.di.sudokuModule
import com.example.sudokusolver.datastore.LocalDataSource
import com.example.sudokusolver.datastore.LocalDataSourceImpl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val commonModule = module {
    singleOf(::LocalDataSourceImpl) bind LocalDataSource::class
    includes(authModule, splashModule, sudokuModule)
    single {
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            explicitNulls = false          // omit nulls when encoding
        }
    }
    single<HttpClient> {
        val appJson = get<Json>()
        HttpClient {
            defaultRequest {
                url(BuildKonfig.BASE_URL)
                contentType(ContentType.Application.Json)
            }
            if (BuildKonfig.DEBUG) {
                install(Logging) {
                    level = LogLevel.ALL
                    logger = object : Logger {
                        override fun log(message: String) {
                            println("KtorLogger: $message")
                        }
                    }
                }
            }
            install(ContentNegotiation) { json(appJson) }
            install(HttpTimeout) {
                requestTimeoutMillis = 60_000
                connectTimeoutMillis = 60_000
                socketTimeoutMillis = 60_000
            }
            HttpResponseValidator {
                validateResponse { response ->

                    if (!response.status.isSuccess()) {
                        val text = response.bodyAsText()
                        val error = runCatching { appJson.decodeFromString<ApiError>(text) }.getOrNull()
                        throw ApiException(
                            httpStatus = response.status.value,
                            code = error?.code ?: "HTTP_${response.status.value}",
                            message = if (response.status.value == 408) "Server Timeout" else error?.message ?: response.status.description
                        )
                    }
                }
            }

        }
    }
}
