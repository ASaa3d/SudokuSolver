package com.example.sudokusolver.sudoku.presenetation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sudokusolver.components.ContainedMorphingLoadingIndicator
import com.example.sudokusolver.components.LogoComp
import org.jetbrains.compose.resources.stringResource
import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.puzzle_empty_state
import sudokusolver.shared.generated.resources.reading_puzzle
import sudokusolver.shared.generated.resources.sudoku_solver_logo_description


// ───────────────────────────── Empty / welcome state ─────────────────────────────

@Composable
fun EmptyState(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onGallery: () -> Unit,
    onCamera: () -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val logoDescription = stringResource(Res.string.sudoku_solver_logo_description)
        LogoComp(modifier = Modifier.size(120.dp).semantics { contentDescription = logoDescription })
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.puzzle_empty_state),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(32.dp))
        if (isLoading) {
            ContainedMorphingLoadingIndicator()
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(Res.string.reading_puzzle),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            MediaButtons(
                enabled = true,
                stacked = true,
                modifier = Modifier.widthIn(max = 360.dp).fillMaxWidth(),
                onGallery = onGallery,
                onCamera = onCamera
            )
        }
    }
}