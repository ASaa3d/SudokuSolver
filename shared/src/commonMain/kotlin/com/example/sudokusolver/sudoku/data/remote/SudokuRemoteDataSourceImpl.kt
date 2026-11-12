package com.example.sudokusolver.sudoku.data.remote

import com.example.sudokusolver.BuildKonfig
import com.example.sudokusolver.splash.data.remote.dto.UserDto
import com.example.sudokusolver.sudoku.data.remote.dto.ImgBbResponseDto
import com.example.sudokusolver.sudoku.data.remote.dto.SudokuGridRequestDto
import com.example.sudokusolver.sudoku.data.remote.dto.SudokuGridResponseDto
import com.example.sudokusolver.sudoku.data.remote.dto.UpdateUserRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class SudokuRemoteDataSourceImpl(
    private val httpClient: HttpClient,
) : SudokuRemoteDataSource {

    override suspend fun uploadImageToImgBb(
        imageBytes: ByteArray,
        expirationSec: Int?
    ): ImgBbResponseDto {
        return httpClient.post("https://api.imgbb.com/1/upload?key=${BuildKonfig.IMG_API_KEY}") {
            expirationSec?.let {
                url {
                    parameter("expiration", it)
                }
            }

            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("image", imageBytes, Headers.build {
                            append(HttpHeaders.ContentType, "image/jpeg")
                            append(HttpHeaders.ContentDisposition, "filename=\"upload.jpg\"")
                        })
                    }
                )
            )
        }.body()
    }

    override suspend fun getSudokuGrid(imageUrl: String): SudokuGridResponseDto {
        return httpClient.post("/sudoku/grid") {
            setBody(SudokuGridRequestDto(imageUrl = imageUrl))
        }.body()
    }

    override suspend fun updateUserMe(token: String, userName: String?, imgUrl: String): UserDto {
        return httpClient.patch("/api/users/me") {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(UpdateUserRequestDto(userName = userName, imgUrl = imgUrl))
        }.body()
    }
}
