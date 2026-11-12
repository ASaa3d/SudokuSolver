package com.example.sudokusolver.sudoku.presenetation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun SolverButtons(
    undoEnabled: Boolean,
    eraseEnabled: Boolean,
    hintEnabled: Boolean,
    solveEnabled: Boolean,
    twoByTwo: Boolean,
    onUndo: () -> Unit,
    onErase: () -> Unit,
    onHint: () -> Unit,
    onSolve: () -> Unit
) {
    if (twoByTwo) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SolverActionButton("Undo", undoEnabled, onUndo, Modifier.weight(1f))
                SolverActionButton("Erase", eraseEnabled, onErase, Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SolverActionButton("Hint", hintEnabled, onHint, Modifier.weight(1f))
                SolverActionButton("Solve", solveEnabled, onSolve, Modifier.weight(1f), primary = true)
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SolverActionButton("Undo", undoEnabled, onUndo, Modifier.weight(1f))
            SolverActionButton("Erase", eraseEnabled, onErase, Modifier.weight(1f))
            SolverActionButton("Hint", hintEnabled, onHint, Modifier.weight(1f))
            SolverActionButton("Solve", solveEnabled, onSolve, Modifier.weight(1f), primary = true)
        }
    }
}

@Composable
private fun SolverActionButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
    primary: Boolean = false
) {
    val shape = RoundedCornerShape(50)
    val padding = PaddingValues(horizontal = 8.dp)
    val buttonModifier = modifier.height(46.dp)
    if (primary) {
        Button(onClick = onClick, enabled = enabled, shape = shape, modifier = buttonModifier, contentPadding = padding) {
            Text(label, maxLines = 1, fontWeight = FontWeight.SemiBold)
        }
    } else {
        OutlinedButton(onClick = onClick, enabled = enabled, shape = shape, modifier = buttonModifier, contentPadding = padding) {
            Text(label, maxLines = 1)
        }
    }
}