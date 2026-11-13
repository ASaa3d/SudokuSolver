package com.example.sudokusolver.sudoku.presenetation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sudokusolver.showSplash
import org.jetbrains.compose.resources.stringResource
import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.camera
import sudokusolver.shared.generated.resources.take_photo
import sudokusolver.shared.generated.resources.upload
import sudokusolver.shared.generated.resources.upload_image


@Composable
fun MediaButtons(
    enabled: Boolean,
    onGallery: () -> Unit,
    onCamera: () -> Unit,
    modifier: Modifier = Modifier,
    stacked: Boolean = false,
    compact: Boolean = false
) {
    val height = if (compact) 40.dp else 52.dp
    val shape = RoundedCornerShape(50)
    val padding = PaddingValues(horizontal = 18.dp)
    val uploadLabel = stringResource(
        if (compact) Res.string.upload else Res.string.upload_image
    )
    val cameraLabel = stringResource(
        if (compact) Res.string.camera else Res.string.take_photo
    )

    val upload: @Composable (Modifier) -> Unit = { m ->
        if (compact) {
            FilledTonalButton(
                onClick = onGallery, enabled = enabled, shape = shape,
                modifier = m.height(height), contentPadding = padding
            ) { Text(uploadLabel, maxLines = 1) }
        } else {
            Button(
                onClick = onGallery, enabled = enabled, shape = shape,
                modifier = m.height(height), contentPadding = padding
            ) { Text(uploadLabel, maxLines = 1) }
        }
    }
    val camera: @Composable (Modifier) -> Unit = { m ->
        OutlinedButton(
            onClick = onCamera, enabled = enabled, shape = shape,
            modifier = m.height(height), contentPadding = padding
        ) { Text(cameraLabel, maxLines = 1) }
    }

    if (stacked) {
        Column(modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            upload(Modifier.fillMaxWidth())
            if (showSplash) camera(Modifier.fillMaxWidth())
        }
    } else {
        Row(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            upload(Modifier)
            if (showSplash) camera(Modifier)
        }
    }
}
