package com.thedigitalstudy.ebookeditor.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Design system calls for:
//   Literata  → editorial body & document headlines (serif, book-optimised)
//   Manrope   → all UI chrome: menus, sidebars, labels, buttons
//
// TODO: replace system fallbacks with Google Fonts downloadable fonts:
//   val provider = GoogleFont.Provider(
//       providerAuthority = "com.google.android.gms.fonts",
//       providerPackage   = "com.google.android.gms",
//       certificates      = R.array.com_google_android_gms_fonts_certs
//   )
//   val LiterataFamily = FontFamily(Font(GoogleFont("Literata"), provider))
//   val ManropeFamily  = FontFamily(Font(GoogleFont("Manrope"),  provider))

val LiterataFamily: FontFamily = FontFamily.Serif       // → Literata
val ManropeFamily: FontFamily  = FontFamily.SansSerif   // → Manrope

val AppTypography = Typography(
    // Display — book title on cover / splash
    displayLarge = TextStyle(
        fontFamily   = LiterataFamily,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 48.sp,
        lineHeight   = 56.sp,
    ),
    // Screen headlines
    headlineLarge = TextStyle(
        fontFamily = ManropeFamily,
        fontWeight = FontWeight.Bold,
        fontSize   = 32.sp,
        lineHeight = 40.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = ManropeFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 24.sp,
        lineHeight = 32.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = ManropeFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize   = 20.sp,
        lineHeight = 28.sp,
    ),
    // Editor body copy — Literata for long-form reading comfort
    bodyLarge = TextStyle(
        fontFamily    = LiterataFamily,
        fontWeight    = FontWeight.Normal,
        fontSize      = 20.sp,
        lineHeight    = 32.sp,
        letterSpacing = 0.2.sp,
    ),
    // UI body copy — Manrope for menus and metadata
    bodyMedium = TextStyle(
        fontFamily = ManropeFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 16.sp,
        lineHeight = 24.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = ManropeFamily,
        fontWeight = FontWeight.Normal,
        fontSize   = 14.sp,
        lineHeight = 20.sp,
    ),
    // Labels and chips
    labelLarge = TextStyle(
        fontFamily    = ManropeFamily,
        fontWeight    = FontWeight.SemiBold,
        fontSize      = 14.sp,
        lineHeight    = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily    = ManropeFamily,
        fontWeight    = FontWeight.Medium,
        fontSize      = 12.sp,
        lineHeight    = 16.sp,
        letterSpacing = 0.05.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = ManropeFamily,
        fontWeight = FontWeight.Medium,
        fontSize   = 11.sp,
        lineHeight = 16.sp,
    ),
)
