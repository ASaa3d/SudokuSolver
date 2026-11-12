package com.example.sudokusolver.sudoku.data.remote

import com.example.sudokusolver.sudoku.data.remote.dto.ImgBbResponseDto
import com.example.sudokusolver.sudoku.data.remote.dto.SudokuGridResponseDto
import com.example.sudokusolver.splash.data.remote.dto.UserDto

interface SudokuRemoteDataSource {
    suspend fun uploadImageToImgBb(
        imageBytes: ByteArray,
        expirationSec: Int?
    ): ImgBbResponseDto

    suspend fun getSudokuGrid(imageUrl: String): SudokuGridResponseDto

    suspend fun updateUserMe(token: String, userName: String?, imgUrl: String): UserDto
}
