package theme

import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────
// Brand palette (raw swatches)
// ─────────────────────────────────────────────
val Olive = Color(0xFF6B7D3D)
val SlateBlue = Color(0xFF2F4A5A)
val Beige = Color(0xFFD4CBB5)
val Taupe = Color(0xFF9C8C6A)
val Cream = Color(0xFFF8F5EE)

// ─────────────────────────────────────────────
// Light Theme
// ─────────────────────────────────────────────

// Primary: olive
val LightPrimary = Olive
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFFDDE8BF)
val LightOnPrimaryContainer = Color(0xFF1B2600)

// Secondary: beige
val LightSecondary = Beige
val LightOnSecondary = Color(0xFF2B2618)
val LightSecondaryContainer = Color(0xFFEAE4D3)
val LightOnSecondaryContainer = Color(0xFF2B2618)

// Tertiary: slate blue (dark complement)
val LightTertiary = SlateBlue
val LightOnTertiary = Color(0xFFFFFFFF)
val LightTertiaryContainer = Color(0xFFCFE5F2)
val LightOnTertiaryContainer = Color(0xFF0A2230)

// Surfaces & text (greys)
val LightBackground = Cream
val LightOnBackground = Color(0xFF3A3A37)
val LightSurface = Cream
val LightOnSurface = Color(0xFF3A3A37)          // main text grey
val LightSurfaceVariant = Color(0xFFE8E2D0)
val LightOnSurfaceVariant = Color(0xFF6B6B66)   // secondary text grey
val LightOutline = Taupe
val LightOutlineVariant = Beige

// Error
val LightError = Color(0xFF841414)
val LightOnError = Color(0xFFFFFFFF)

// ─────────────────────────────────────────────
// Dark Theme
// ─────────────────────────────────────────────

// Primary: lightened olive for dark backgrounds
val DarkPrimary = Color(0xFFB5CC7A)
val DarkOnPrimary = Color(0xFF1F2B00)
val DarkPrimaryContainer = Color(0xFF4F5F2A)
val DarkOnPrimaryContainer = Color(0xFFD3E8A0)

// Secondary: beige
val DarkSecondary = Color(0xFFCFC6AE)
val DarkOnSecondary = Color(0xFF352F1C)
val DarkSecondaryContainer = Color(0xFF4C452F)
val DarkOnSecondaryContainer = Color(0xFFEAE2CB)

// Tertiary: slate blue
val DarkTertiary = Color(0xFFA9CCE0)
val DarkOnTertiary = Color(0xFF0D3345)
val DarkTertiaryContainer = SlateBlue
val DarkOnTertiaryContainer = Color(0xFFD2E8F5)

// Surfaces & text (slate-tinted darks, light greys)
val DarkBackground = Color(0xFF151D21)
val DarkOnBackground = Color(0xFFE3E3DE)
val DarkSurface = Color(0xFF1A2328)
val DarkOnSurface = Color(0xFFE3E3DE)           // main text grey
val DarkSurfaceVariant = Color(0xFF2A363D)
val DarkOnSurfaceVariant = Color(0xFFB5BCB8)    // secondary text grey
val DarkOutline = Color(0xFF8A8F86)
val DarkOutlineVariant = Color(0xFF3B484F)

// Error
val DarkError = Color(0xFFFFB4AB)
val DarkOnError = Color(0xFF690005)