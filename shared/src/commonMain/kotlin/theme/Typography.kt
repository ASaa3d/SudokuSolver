package theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val SudokuTypography = Typography(
    // 1. Solved / Given Numbers inside the Grid
    displayLarge = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = 0.sp
    ),

    // 2. Candidate Notes / Pencil Marks (3x3 grid inside a single cell)
    labelSmall = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        fontSize = 9.sp,
        letterSpacing = 0.sp,
        lineHeight = 10.sp
    ),

    // 3. Tutorial Headers & Strategy Titles
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),

    // 4. Learning Materials / Tutorial Body Text
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp // Added line height for comfortable reading
    ),

    // 5. Grid Control Buttons (1-9 Keypad numbers below the board)
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    )
)
