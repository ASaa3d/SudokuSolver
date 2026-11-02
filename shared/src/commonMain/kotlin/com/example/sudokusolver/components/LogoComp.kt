package com.example.sudokusolver.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.sudoku_logo_dark
import sudokusolver.shared.generated.resources.sudoku_logo_light

@Composable
fun LogoComp(modifier: Modifier = Modifier)
{
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(if (isSystemInDarkTheme()) Res.drawable.sudoku_logo_dark else Res.drawable.sudoku_logo_light),
            contentDescription = "logo",
            modifier = Modifier.fillMaxWidth(0.2f).padding(all = 8.dp)
        )
        Text(
            modifier = Modifier.fillMaxWidth(0.8f).align(Alignment.CenterVertically),
            text = "Sudoku Solver",
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 36.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = MaterialTheme.typography.labelLarge.fontFamily
        )
    }
}