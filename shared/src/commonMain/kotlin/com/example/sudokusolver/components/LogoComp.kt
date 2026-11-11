package com.example.sudokusolver.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
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
import org.jetbrains.compose.resources.stringResource
import sudokusolver.shared.generated.resources.Res
import sudokusolver.shared.generated.resources.logo_description
import sudokusolver.shared.generated.resources.sudoku_logo_dark
import sudokusolver.shared.generated.resources.sudoku_logo_light
import sudokusolver.shared.generated.resources.sudoku_solver

@Composable
fun LogoComp(modifier: Modifier = Modifier)
{
    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(if (isSystemInDarkTheme()) Res.drawable.sudoku_logo_dark else Res.drawable.sudoku_logo_light),
            contentDescription = stringResource(Res.string.logo_description),
            modifier = Modifier.fillMaxHeight().sizeIn(minHeight = 20.dp, minWidth =20.dp,maxHeight = 50.dp)
        )
        Text(
            modifier = Modifier.weight(0.8f).align(Alignment.CenterVertically),
            text = stringResource(Res.string.sudoku_solver),
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 36.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = MaterialTheme.typography.labelLarge.fontFamily
        )
    }
}