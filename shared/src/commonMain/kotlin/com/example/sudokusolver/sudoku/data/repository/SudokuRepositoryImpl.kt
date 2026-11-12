package com.example.sudokusolver.sudoku.data.repository

import androidx.compose.ui.graphics.ImageBitmap
import com.example.sudokusolver.datastore.LocalDataSource
import com.example.sudokusolver.sudoku.data.local.SudokuRecognizer
import com.example.sudokusolver.sudoku.data.remote.SudokuRemoteDataSource
import com.example.sudokusolver.sudoku.domain.model.SudokuGrid
import com.example.sudokusolver.sudoku.domain.repository.SudokuRepository
import com.example.sudokusolver.sudoku.util.toByteArray
import com.example.sudokusolver.splash.data.remote.SplashRemoteDataSource
import com.example.sudokusolver.splash.data.remote.dto.UserDto
import core.safeCall

class SudokuRepositoryImpl(
    private val remoteDataSource: SudokuRemoteDataSource,
    private val sudokuRecognizer: SudokuRecognizer?,
    private val splashRemoteDataSource: SplashRemoteDataSource,
    private val localDataSource: LocalDataSource,
) : SudokuRepository {
    companion object {
        private const val TOKEN_KEY = "access_token"
    }

    override suspend fun getUserMe(): Result<UserDto> = safeCall {
        val token = localDataSource.get(TOKEN_KEY)
        splashRemoteDataSource.getUserMe(token)
    }

    override suspend fun signOut(): Result<Unit> = safeCall {
        localDataSource.clear(TOKEN_KEY)
    }

    override suspend fun updateProfilePhoto(imageByteArray: ByteArray): Result<String> = safeCall {
        val uploadDto = remoteDataSource.uploadImageToImgBb(imageByteArray, expirationSec = null)
        val uploadedImageUrl = uploadDto.data?.url
            ?: throw Exception("Failed to upload profile photo to ImgBB: URL is null")
        val token = localDataSource.get(TOKEN_KEY)
        val updatedUser = remoteDataSource.updateUserMe(
            token = token,
            userName = null,
            imgUrl = uploadedImageUrl
        )
        updatedUser.imgUrl
            ?: throw Exception("Failed to update profile photo: URL is null")
    }

    override suspend fun getSudokuGridFromMobile(imageBitmap: ImageBitmap): Result<SudokuGrid> =
        safeCall {
            if (sudokuRecognizer != null) {
                val resultMatrix = sudokuRecognizer.process(imageBitmap)
                    ?: throw Exception("Failed to recognize Sudoku grid from image")
                val gridList = resultMatrix.map { it.toList() }
                if (gridList.all { row -> row.all { it == 0 } }) {
                    throw Exception("Failed to recognize Sudoku grid from image")
                }
                SudokuGrid(grid = gridList, gridSize = resultMatrix.size)
            } else {
                val imageBytes = imageBitmap.toByteArray(quality = 100)
                getSudokuGridFromBytes(imageBytes)
            }
        }

    override suspend fun getSudokuGridFromWeb(imageByteArray: ByteArray): Result<SudokuGrid> =
        safeCall {
            getSudokuGridFromBytes(imageByteArray)
        }

    private suspend fun getSudokuGridFromBytes(imageBytes: ByteArray): SudokuGrid {
        val uploadDto = remoteDataSource.uploadImageToImgBb(imageBytes, 600)
        val imageUrl = uploadDto.data?.url
            ?: throw Exception("Failed to upload image to ImgBB: URL is null")
        val gridDto = remoteDataSource.getSudokuGrid(imageUrl)
        if (gridDto.grid.all { row -> row.all { it == 0 } }) {
            throw Exception("Failed to recognize Sudoku grid from image")
        }
        return SudokuGrid(grid = gridDto.grid, gridSize = gridDto.grid.size)
    }
}
