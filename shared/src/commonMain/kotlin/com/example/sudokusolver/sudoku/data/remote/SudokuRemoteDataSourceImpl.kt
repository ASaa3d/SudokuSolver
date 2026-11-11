package com.example.sudokusolver.sudoku.data.remote

import com.example.sudokusolver.sudoku.data.remote.dto.ImgBbResponseDto
import com.example.sudokusolver.sudoku.data.remote.dto.SudokuGridRequestDto
import com.example.sudokusolver.sudoku.data.remote.dto.SudokuGridResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class SudokuRemoteDataSourceImpl(
    private val httpClient: HttpClient,
) : SudokuRemoteDataSource {

    override suspend fun uploadImageToImgBb(
        imageBytes: ByteArray,
        apiKey: String,
        fileName: String,
    ): ImgBbResponseDto {
        return httpClient.post("https://api.imgbb.com/1/upload?key=$apiKey") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("image", imageBytes, Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
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
}
