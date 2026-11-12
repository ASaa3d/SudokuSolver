package com.example.sudokusolver.sudoku.domain.repository

import androidx.compose.ui.graphics.ImageBitmap
import com.example.sudokusolver.splash.data.remote.dto.UserDto
import com.example.sudokusolver.sudoku.domain.model.SudokuGrid

interface SudokuRepository {
    suspend fun getSudokuGridFromMobile(imageBitmap: ImageBitmap): Result<SudokuGrid>
    suspend fun getSudokuGridFromWeb(imageByteArray: ByteArray): Result<SudokuGrid>
    suspend fun getUserMe(): Result<UserDto>
    suspend fun updateProfilePhoto(imageByteArray: ByteArray): Result<String>

    suspend fun signOut():Result<Unit>
}
