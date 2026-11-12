package com.example.sudokusolver.sudoku.presenetation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun NumberPad(
    size: Int,
    enabled: Boolean,
    onNumberClick: (Int) -> Unit
) {
    if (size <= 0) return
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        // One row when there's room, otherwise two balanced rows.
        val perRow = if (maxWidth / size >= 38.dp) size else (size + 1) / 2
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            (1..size).toList().chunked(perRow).forEach { rowNumbers ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    rowNumbers.forEach { number ->
                        FilledTonalButton(
                            onClick = { onNumberClick(number) },
                            enabled = enabled,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).height(48.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                number.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    repeat(perRow - rowNumbers.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
}