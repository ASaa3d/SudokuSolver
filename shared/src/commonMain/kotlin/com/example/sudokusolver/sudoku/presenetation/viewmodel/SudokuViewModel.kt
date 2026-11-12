package com.example.sudokusolver.sudoku.presenetation.viewmodel

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import com.example.sudokusolver.splash.data.remote.dto.UserDto
import com.example.sudokusolver.sudoku.domain.model.Hint
import com.example.sudokusolver.sudoku.domain.model.SudokuAction
import com.example.sudokusolver.sudoku.domain.model.SudokuGrid
import com.example.sudokusolver.sudoku.domain.usecase.GetSudokuGridMobileUseCase
import com.example.sudokusolver.sudoku.domain.usecase.GetSudokuGridWebUseCase
import com.example.sudokusolver.sudoku.domain.usecase.GetUserMeUseCase
import com.example.sudokusolver.sudoku.domain.usecase.SignOutUseCase
import com.example.sudokusolver.sudoku.domain.usecase.UpdateProfilePhotoUseCase
import com.example.sudokusolver.sudoku.presenetation.event.SudokuEvent
import com.example.sudokusolver.sudoku.presenetation.state.SudokuState
import core.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SudokuViewModel(
    private val getSudokuGridMobileUseCase: GetSudokuGridMobileUseCase,
    private val getSudokuGridWebUseCase: GetSudokuGridWebUseCase,
    private val getUserMeUseCase: GetUserMeUseCase,
    private val updateProfilePhotoUseCase: UpdateProfilePhotoUseCase,
    private val signOutUseCase: SignOutUseCase
) : BaseViewModel<SudokuState, SudokuEvent>(SudokuState()) {

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
                .onSuccess {
                    updateState { currentState.copy(userInfo = null) }
                    sendEvent(SudokuEvent.SignOut)
                }
                .onFailure { error ->
                    updateState { currentState.copy(error = error.message) }
                }
        }
    }

    fun updateProfilePhoto(imageByteArray: ByteArray) {
        viewModelScope.launch {
            updateProfilePhotoUseCase(imageByteArray)
                .onSuccess { imageUrl ->
                    updateState {
                        currentState.copy(
                            userInfo = currentState.userInfo?.copy(imgUrl = imageUrl),
                        )
                    }
                }
                .onFailure { error ->
                    updateState {
                        currentState.copy(
                            error = error.message ?: "Failed to update profile photo"
                        )
                    }
                }
        }
    }

    fun loadUserInfo(isGuest: Boolean) {
        if (isGuest) {
            updateState {
                currentState.copy(
                    userInfo = UserDto(id = 0, userName = "Guest", email = "guest@user.com")
                )
            }
        } else {
            viewModelScope.launch {
                getUserMeUseCase()
                    .onSuccess { user ->
                        updateState { currentState.copy(userInfo = user, error = null) }
                    }
                    .onFailure { error ->
                        updateState { currentState.copy(error = error.message) }
                    }
            }
        }
    }

    fun getGridFromMobile(imgBitmap: ImageBitmap) {

        loadGrid { getSudokuGridMobileUseCase(imgBitmap) }

    }

    fun getGridFromWeb(imageByteArray: ByteArray) {
        loadGrid { getSudokuGridWebUseCase(imageByteArray) }
    }

    private fun loadGrid(load: suspend () -> Result<SudokuGrid>) {
        viewModelScope.launch {
            updateState { currentState.copy(isLoading = true, error = null) }
            load()
                .onSuccess { result ->
                    updateState {
                        currentState.copy(
                            sudokuGrid = result.grid,
                            oldGrid = result.grid,   // the givens; used for locking cells and for undo replay
                            gridSize = result.gridSize,
                            actionStack = emptyList(),
                            badNumbers = null,
                            hintPosition = null,
                            hintText = "",
                            selectedPosition = null,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    updateState { currentState.copy(error = error.message) }
                }
            updateState { currentState.copy(isLoading = false) }
        }
    }

    fun onErrorShowed() {
        updateState { currentState.copy(error = null) }
    }

    fun selectCell(row: Int, col: Int) {
        updateState { currentState.copy(selectedPosition = row to col) }
    }

    // ---------------- Actions ----------------

    fun addAction(number: Int) {
        val state = currentState
        val grid = state.sudokuGrid ?: return
        val (row, col) = state.selectedPosition ?: return
        if (isBlocked() || isGiven(row, col) || number !in 1..state.gridSize) return
        if (grid[row][col] == number) return

        val newGrid = grid.withValue(row, col, number)
        val conflicts = findConflicts(newGrid, row, col, number)

        updateState {
            currentState.copy(
                sudokuGrid = newGrid,
                actionStack = currentState.actionStack + SudokuAction.ADD(row, col, number),
                hintPosition = null,
                hintText = "",
                badNumbers = conflicts.ifEmpty { null },
                error = if (conflicts.isEmpty()) null
                else "Number $number conflicts with another cell. Undo to continue."
            )
        }
    }

    fun eraseAction() {
        val state = currentState
        val grid = state.sudokuGrid ?: return
        val (row, col) = state.selectedPosition ?: return
        if (isBlocked() || isGiven(row, col) || grid[row][col] == 0) return

        updateState {
            currentState.copy(
                sudokuGrid = grid.withValue(row, col, 0),
                actionStack = currentState.actionStack + SudokuAction.REMOVE(
                    row,
                    col,
                    grid[row][col]
                ),
                hintPosition = null,
                hintText = ""
            )
        }
    }

    fun undoAction() {
        val state = currentState
        val base = state.oldGrid ?: return
        if (state.actionStack.isEmpty()) return

        val remaining = state.actionStack.dropLast(1)
        val last = state.actionStack.last()

        viewModelScope.launch {
            // Replay can include a SOLVE, so do it off the main thread
            val rebuilt =
                withContext(Dispatchers.Default) { replay(base, remaining, state.gridSize) }
            updateState {
                currentState.copy(
                    sudokuGrid = rebuilt,
                    actionStack = remaining,
                    selectedPosition = when (last) {
                        is SudokuAction.ADD -> last.x to last.y
                        is SudokuAction.REMOVE -> last.x to last.y
                        SudokuAction.SOLVE -> currentState.selectedPosition
                    },
                    badNumbers = null,   // the only thing that could cause an error was the last add
                    error = null,
                    hintPosition = null,
                    hintText = ""
                )
            }
        }
    }

    fun requestHint() {
        val state = currentState
        val grid = state.sudokuGrid ?: return
        if (isBlocked()) return

        val empties = emptyCells(grid)
        if (empties.isEmpty()) {
            updateState {
                currentState.copy(
                    hintPosition = null,
                    hintText = "The grid is already complete."
                )
            }
            return
        }

        // A cell with zero candidates means an earlier move was wrong, even though it didn't clash directly
        if (empties.any { (r, c) -> candidates(grid, r, c).isEmpty() }) {
            updateState {
                currentState.copy(
                    hintPosition = null,
                    hintText = "",
                    error = "Some cell has no valid number left. Undo a few moves."
                )
            }
            return
        }

        val hint = findHint(grid, state.selectedPosition)
        if (hint == null) {
            updateState {
                currentState.copy(
                    hintPosition = null,
                    hintText = "No cell can be deduced with simple logic from here."
                )
            }
            return
        }

        updateState {
            currentState.copy(
                hintPosition = hint.row to hint.col,
                selectedPosition = hint.row to hint.col,
                hintText = hint.reason
            )
        }
    }

// ---------------- Hint helpers ----------------


    private fun emptyCells(grid: List<List<Int>>): List<Pair<Int, Int>> =
        grid.indices.flatMap { r -> grid.indices.map { c -> r to c } }
            .filter { (r, c) -> grid[r][c] == 0 }

    /** Numbers that can legally go in an empty cell (row, column and box all free of them). */
    private fun candidates(grid: List<List<Int>>, r: Int, c: Int): List<Int> {
        val size = grid.size
        val br = r / boxRows(size) * boxRows(size)
        val bc = c / boxCols(size) * boxCols(size)
        return (1..size).filter { n ->
            (0 until size).none { grid[r][it] == n || grid[it][c] == n } &&
                    (br until br + boxRows(size)).none { rr ->
                        (bc until bc + boxCols(size)).any { cc -> grid[rr][cc] == n }
                    }
        }
    }

    private fun findHint(grid: List<List<Int>>, preferred: Pair<Int, Int>?): Hint? {
        val size = grid.size
        val empties = emptyCells(grid)

        // 1) Naked single: an empty cell with exactly one possible number
        val nakedSingles = empties.filter { (r, c) -> candidates(grid, r, c).size == 1 }
        val naked = nakedSingles.firstOrNull { it == preferred } ?: nakedSingles.firstOrNull()
        if (naked != null) {
            val (r, c) = naked
            val value = candidates(grid, r, c).first()
            return Hint(r, c, value, "Row ${r + 1}, column ${c + 1} can only be $value.")
        }

        // 2) Hidden single: inside a row/column/box, a number that fits only one cell
        val units = buildList {
            for (i in 0 until size) {
                add("row ${i + 1}" to (0 until size).map { i to it })
                add("column ${i + 1}" to (0 until size).map { it to i })
            }
            for (br in 0 until size step boxRows(size)) for (bc in 0 until size step boxCols(size)) {
                add("this box" to (br until br + boxRows(size)).flatMap { r ->
                    (bc until bc + boxCols(size)).map { c -> r to c }
                })
            }
        }
        for ((name, cells) in units) {
            for (n in 1..size) {
                if (cells.any { (r, c) -> grid[r][c] == n }) continue   // already placed in this unit
                val spots =
                    cells.filter { (r, c) -> grid[r][c] == 0 && n in candidates(grid, r, c) }
                if (spots.size == 1) {
                    val (r, c) = spots.first()
                    return Hint(
                        r,
                        c,
                        n,
                        "In $name, $n can only go in row ${r + 1}, column ${c + 1}."
                    )
                }
            }
        }
        return null
    }

    fun solve() {
        val state = currentState
        val grid = state.sudokuGrid ?: return
        if (isBlocked()) return

        viewModelScope.launch {
            updateState { currentState.copy(isLoading = true, error = null) }
            val solution = withContext(Dispatchers.Default) { solveGrid(grid, state.gridSize) }
            updateState {
                if (solution == null) {
                    currentState.copy(
                        isLoading = false,
                        error = "No solution from the current state. Try undoing some moves."
                    )
                } else {
                    currentState.copy(
                        isLoading = false,
                        sudokuGrid = solution,
                        actionStack = currentState.actionStack + SudokuAction.SOLVE,
                        hintPosition = null,
                        hintText = "",
                        badNumbers = null
                    )
                }
            }
        }
    }


    private fun isBlocked() = !currentState.badNumbers.isNullOrEmpty()

    private fun isGiven(row: Int, col: Int): Boolean {
        val v = currentState.oldGrid?.getOrNull(row)?.getOrNull(col)
        return v != null && v != 0
    }

    private fun List<List<Int>>.withValue(row: Int, col: Int, value: Int): List<List<Int>> =
        mapIndexed { r, line ->
            if (r == row) line.toMutableList().also { it[col] = value } else line
        }

    private fun replay(
        base: List<List<Int>>,
        actions: List<SudokuAction>,
        size: Int
    ): List<List<Int>> =
        actions.fold(base) { grid, action ->
            when (action) {
                is SudokuAction.ADD -> grid.withValue(action.x, action.y, action.value)
                is SudokuAction.REMOVE -> grid.withValue(action.x, action.y, 0)
                SudokuAction.SOLVE -> solveGrid(grid, size) ?: grid
            }
        }

    private fun boxRows(size: Int) = if (size == 6) 2 else 3
    private fun boxCols(size: Int) = 3

    private fun findConflicts(
        grid: List<List<Int>>,
        row: Int,
        col: Int,
        num: Int
    ): List<Pair<Int, Int>> {
        val size = grid.size
        val bad = mutableSetOf<Pair<Int, Int>>()
        for (i in 0 until size) {
            if (i != col && grid[row][i] == num) bad += row to i
            if (i != row && grid[i][col] == num) bad += i to col
        }
        val br = row / boxRows(size) * boxRows(size)
        val bc = col / boxCols(size) * boxCols(size)
        for (r in br until br + boxRows(size)) for (c in bc until bc + boxCols(size)) {
            if ((r != row || c != col) && grid[r][c] == num) bad += r to c
        }
        if (bad.isNotEmpty()) bad += row to col
        return bad.toList()
    }

    private fun solveGrid(input: List<List<Int>>, size: Int): List<List<Int>>? {
        val g = input.map { it.toMutableList() }

        fun candidates(r: Int, c: Int): List<Int> {
            val br = r / boxRows(size) * boxRows(size)
            val bc = c / boxCols(size) * boxCols(size)
            return (1..size).filter { n ->
                (0 until size).none { g[r][it] == n || g[it][c] == n } &&
                        (br until br + boxRows(size)).none { rr ->
                            (bc until bc + boxCols(size)).any { cc -> g[rr][cc] == n }
                        }
            }
        }

        fun backtrack(): Boolean {
            var best: Triple<Int, Int, List<Int>>? = null
            for (r in 0 until size) for (c in 0 until size) {
                if (g[r][c] != 0) continue
                val cand = candidates(r, c)
                if (cand.isEmpty()) return false
                if (best == null || cand.size < best.third.size) best = Triple(r, c, cand)
            }
            val (r, c, cand) = best ?: return true   // no empty cells = solved
            for (n in cand) {
                g[r][c] = n
                if (backtrack()) return true
            }
            g[r][c] = 0
            return false
        }

        return if (backtrack()) g else null
    }
}