package com.tronsguitar.ebookeditor.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ── Light Color Scheme – "Writer's Sanctuary" ─────────────────────────────────
private val LightColorScheme = lightColorScheme(
    primary              = Primary,
    onPrimary            = OnPrimary,
    primaryContainer     = PrimaryContainer,
    onPrimaryContainer   = OnPrimaryContainer,
    secondary            = Secondary,
    onSecondary          = OnSecondary,
    secondaryContainer   = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary             = Tertiary,
    onTertiary           = OnTertiary,
    tertiaryContainer    = TertiaryContainer,
    onTertiaryContainer  = OnTertiaryContainer,
    error                = Error,
    onError              = OnError,
    errorContainer       = ErrorContainer,
    onErrorContainer     = OnErrorContainer,
    background           = Background,
    onBackground         = OnBackground,
    surface              = Surface,
    onSurface            = OnSurface,
    surfaceVariant       = SurfaceVariant,
    onSurfaceVariant     = OnSurfaceVariant,
    surfaceTint          = SurfaceTint,
    inverseSurface       = InverseSurface,
    inverseOnSurface     = InverseOnSurface,
    inversePrimary       = InversePrimary,
    outline              = Outline,
    outlineVariant       = OutlineVariant,
    scrim                = Scrim,
    surfaceContainerLowest  = SurfaceContainerLowest,
    surfaceContainerLow     = SurfaceContainerLow,
    surfaceContainer        = SurfaceContainer,
    surfaceContainerHigh    = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,
)

// ── Dark Color Scheme – "Writer's Sanctuary Dark" ────────────────────────────
private val DarkColorScheme = darkColorScheme(
    primary              = DarkPrimary,
    onPrimary            = DarkOnPrimary,
    primaryContainer     = DarkPrimaryContainer,
    onPrimaryContainer   = DarkOnPrimaryContainer,
    secondary            = DarkSecondary,
    onSecondary          = DarkOnSecondary,
    secondaryContainer   = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary             = DarkTertiary,
    onTertiary           = DarkOnTertiary,
    tertiaryContainer    = DarkTertiaryContainer,
    onTertiaryContainer  = DarkOnTertiaryContainer,
    error                = DarkError,
    onError              = DarkOnError,
    errorContainer       = DarkErrorContainer,
    onErrorContainer     = DarkOnErrorContainer,
    background           = DarkBackground,
    onBackground         = DarkOnBackground,
    surface              = DarkSurface,
    onSurface            = DarkOnSurface,
    surfaceVariant       = DarkSurfaceVariant,
    onSurfaceVariant     = DarkOnSurfaceVariant,
    surfaceTint          = DarkSurfaceTint,
    inverseSurface       = DarkInverseSurface,
    inverseOnSurface     = DarkInverseOnSurface,
    inversePrimary       = DarkInversePrimary,
    outline              = DarkOutline,
    outlineVariant       = DarkOutlineVariant,
    surfaceContainerLowest  = DarkSurfaceContainerLowest,
    surfaceContainerLow     = DarkSurfaceContainerLow,
    surfaceContainer        = DarkSurfaceContainer,
    surfaceContainerHigh    = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHighest,
)

/**
 * The root composable theme for The Digital Study / EbookEditor.
 *
 * Applies the "Writer's Sanctuary" color scheme and typography, and updates
 * the system bar appearance so that status and navigation bars integrate with
 * the warm paper-white (light) or dark-ink (dark) surface.
 */
@Composable
fun EbookEditorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = EbookEditorTypography,
        content     = content,
    )
}
