package com.example.sudokusolver.sudoku.presenetation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sudokusolver.sudoku.presenetation.state.SudokuState


@Composable
fun ControlsPanel(
    state: SudokuState,
    twoByTwo: Boolean,
    onAddNumber: (Int) -> Unit,
    onUndo: () -> Unit,
    onErase: () -> Unit,
    onHint: () -> Unit,
    onSolve: () -> Unit
) {
    val canAct = !state.isLoading && state.badNumbers.isNullOrEmpty()
    val cellEditable = state.selectedPosition?.let { (row, col) ->
        state.oldGrid?.getOrNull(row)?.getOrNull(col) == 0
    } == true

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        HintArea(state.hintText)
        NumberPad(
            size = state.gridSize,
            enabled = canAct && cellEditable,
            onNumberClick = onAddNumber
        )
        SolverButtons(
            undoEnabled = !state.isLoading && state.actionStack.isNotEmpty(),
            eraseEnabled = canAct && cellEditable,
            hintEnabled = canAct,
            solveEnabled = canAct,
            twoByTwo = twoByTwo,
            onUndo = onUndo,
            onErase = onErase,
            onHint = onHint,
            onSolve = onSolve
        )
    }
}