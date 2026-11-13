package com.example.sudokusolver.sudoku.presenetation.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.sudokusolver.components.ContainedMorphingLoadingIndicator
import com.example.sudokusolver.sudoku.presenetation.state.SudokuState
import org.jetbrains.compose.resources.stringResource
import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.row_column_empty
import sudokusolver.shared.generated.resources.row_column_value
import kotlin.collections.orEmpty
import kotlin.collections.toSet

// ───────────────────────────── Board ─────────────────────────────

@Composable
fun SudokuBoard(
    grid: List<List<Int>>,
    state: SudokuState,
    modifier: Modifier = Modifier,
    onSelectCell: (Int, Int) -> Unit
) {
    val gridSize = grid.size
    if (gridSize == 0) return

    val colors = MaterialTheme.colorScheme
    val boxRows = if (gridSize == 6) 2 else 3
    val boxCols = 3
    val selected = state.selectedPosition
    val hint = state.hintPosition
    val badCells = state.badNumbers.orEmpty().toSet()
    val selectedValue = selected
        ?.let { (r, c) -> grid.getOrNull(r)?.getOrNull(c) }
        ?.takeIf { it != 0 }

    val thinLine = colors.outlineVariant
    val thickLine = colors.onSurface.copy(alpha = 0.7f)
    val clear = colors.surface.copy(alpha = 0f)

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = colors.surface,
        tonalElevation = 2.dp,
        shadowElevation = 10.dp,
        border = BorderStroke(1.5.dp, thickLine)
    ) {
        BoxWithConstraints(Modifier.fillMaxSize().padding(6.dp)) {
            val cellSize = maxWidth / gridSize
            val fontSize: TextUnit = with(LocalDensity.current) { (cellSize * 0.52f).toSp() }

            Column(Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp))) {
                grid.forEachIndexed { row, values ->
                    Row(Modifier.weight(1f).fillMaxWidth()) {
                        values.take(gridSize).forEachIndexed { col, value ->
                            val position = row to col
                            val isGiven = state.oldGrid?.getOrNull(row)?.getOrNull(col)
                                ?.let { it != 0 } == true
                            val related = selected != null && (
                                    row == selected.first ||
                                            col == selected.second ||
                                            (row / boxRows == selected.first / boxRows &&
                                                    col / boxCols == selected.second / boxCols))
                            val target = when {
                                position in badCells -> colors.errorContainer
                                position == hint -> colors.tertiaryContainer
                                position == selected -> colors.primaryContainer
                                selectedValue != null && value == selectedValue ->
                                    colors.primary.copy(alpha = 0.22f)
                                related -> colors.primaryContainer.copy(alpha = 0.35f)
                                else -> clear
                            }
                            val background by animateColorAsState(target, tween(140), label = "cell")
                            val textColor = when {
                                position in badCells -> colors.error
                                isGiven -> colors.onSurface
                                else -> colors.primary
                            }
                            val cellDescription = if (value == 0) {
                                stringResource(Res.string.row_column_empty, row + 1, col + 1)
                            } else {
                                stringResource(Res.string.row_column_value, row + 1, col + 1, value)
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(background)
                                    .clickable(enabled = !state.isLoading) { onSelectCell(row, col) }
                                    .semantics {
                                        contentDescription =
                                            cellDescription
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (value != 0) {
                                    Text(
                                        text = value.toString(),
                                        color = textColor,
                                        fontSize = fontSize,
                                        fontWeight = if (isGiven) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Grid lines: thin between cells, thick between boxes (outer edge is the card border).
            Canvas(Modifier.fillMaxSize()) {
                val cw = size.width / gridSize
                val ch = size.height / gridSize
                val thinPx = 1.dp.toPx()
                val thickPx = 2.5.dp.toPx()
                for (i in 1 until gridSize) {
                    val vertThick = i % boxCols == 0
                    val horizThick = i % boxRows == 0
                    drawLine(
                        color = if (vertThick) thickLine else thinLine,
                        start = Offset(i * cw, 0f),
                        end = Offset(i * cw, size.height),
                        strokeWidth = if (vertThick) thickPx else thinPx
                    )
                    drawLine(
                        color = if (horizThick) thickLine else thinLine,
                        start = Offset(0f, i * ch),
                        end = Offset(size.width, i * ch),
                        strokeWidth = if (horizThick) thickPx else thinPx
                    )
                }
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize().background(colors.surface.copy(alpha = 0.55f)),
                    contentAlignment = Alignment.Center
                ) { ContainedMorphingLoadingIndicator() }
            }
        }
    }
}