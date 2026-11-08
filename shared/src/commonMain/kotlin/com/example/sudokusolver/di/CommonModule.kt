package com.example.sudokusolver.di

import com.example.sudokusolver.BuildKonfig
import core.ApiError
import core.ApiException
import com.example.sudokusolver.auth.di.authModule
import com.example.sudokusolver.datastore.LocalDataSource
import com.example.sudokusolver.datastore.LocalDataSourceImpl
import com.example.sudokusolver.sudoku.SudokuViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
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
    viewModel { SudokuViewModel(sudokuRecognizer = getOrNull()) }
    singleOf(::LocalDataSourceImpl) bind LocalDataSource::class
    includes(authModule)
    single {
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            explicitNulls = false          // omit nulls when encoding
        }
    }
    single<HttpClient> {
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
            install(ContentNegotiation) { json(get<Json>()) }
            install(HttpTimeout) {
                requestTimeoutMillis = 60_000
                connectTimeoutMillis = 60_000
                socketTimeoutMillis = 60_000
            }
            HttpResponseValidator {
                validateResponse { response ->
                    if (!response.status.isSuccess()) {
                        val error = runCatching { response.body<ApiError>() }.getOrNull()
                        throw ApiException(
                            code = error?.code ?: response.status.value,
                            message = error?.message ?: response.status.description,
                        )
                    }
                }
            }

        }
    }
}


