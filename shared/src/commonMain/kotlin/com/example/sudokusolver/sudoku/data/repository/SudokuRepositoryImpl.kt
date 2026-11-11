package com.example.sudokusolver.sudoku.data.repository

import com.example.sudokusolver.sudoku.data.local.SudokuRecognizer
import com.example.sudokusolver.sudoku.data.remote.SudokuRemoteDataSource
import com.example.sudokusolver.sudoku.domain.model.ImgBbData
import com.example.sudokusolver.sudoku.domain.model.SudokuGrid
import com.example.sudokusolver.sudoku.domain.repository.SudokuRepository
import core.safeCall

class SudokuRepositoryImpl(
    private val remoteDataSource: SudokuRemoteDataSource,
    private val sudokuRecognizer: SudokuRecognizer?
) : SudokuRepository {

    override suspend fun uploadImageToImgBb(
        imageBytes: ByteArray,
        apiKey: String,
        fileName: String,
    ): Result<ImgBbData> = safeCall {
        val dto = remoteDataSource.uploadImageToImgBb(imageBytes, apiKey, fileName)
        val data = dto.data ?: throw Exception("Failed to upload image: response data is null")
        ImgBbData(
            id = data.id.orEmpty(),
            title = data.title.orEmpty(),
            urlViewer = data.urlViewer.orEmpty(),
            url = data.url.orEmpty(),
            displayUrl = data.displayUrl.orEmpty(),
            width = data.width ?: 0,
            height = data.height ?: 0,
            size = data.size ?: 0,
        )
    }

    override suspend fun getSudokuGrid(imageUrl: String): Result<SudokuGrid> = safeCall {
        if (sudokuRecognizer != null) {
            val dto = sudokuRecognizer

        } else {
            val dto = remoteDataSource.getSudokuGrid(imageUrl)
            SudokuGrid(grid = dto.grid)
        }
    }
}
