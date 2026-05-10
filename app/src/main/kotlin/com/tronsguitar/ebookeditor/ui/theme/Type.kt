package com.tronsguitar.ebookeditor.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ── Font Families ─────────────────────────────────────────────────────────────
// The design system calls for:
//   Literata  – bookish serif  – editor body content (final-book feel)
//   Manrope   – geometric sans – all UI chrome (menus, labels, navigation)
//
// HOW TO ADD CUSTOM FONTS:
//   1. Download the font files from Google Fonts:
//        Literata: https://fonts.google.com/specimen/Literata
//        Manrope:  https://fonts.google.com/specimen/Manrope
//   2. Place the .ttf files in app/src/main/res/font/ with snake_case names,
//      e.g. literata_regular.ttf, manrope_bold.ttf.
//   3. Replace the FontFamily.Serif / FontFamily.SansSerif references below
//      with FontFamily(Font(R.font.literata_regular, …), …) declarations.
//
// The system serif/sans-serif fallbacks below ensure the project compiles and
// renders correctly out of the box without any downloaded font assets.

/** Editor body font – replace with Literata once font files are added. */
val Literata: FontFamily = FontFamily.Serif

/** UI chrome font – replace with Manrope once font files are added. */
val Manrope: FontFamily = FontFamily.SansSerif

// ── Typography Scale ──────────────────────────────────────────────────────────
// Maps the "Writer's Sanctuary" design system to the Material 3 type scale.
//
// Design token → M3 role mapping:
//   display-lg  (Literata 48/56 SemiBold) → displayLarge
//   headline-lg (Manrope  32/40 Bold)     → headlineLarge
//   headline-md (Manrope  24/32 SemiBold) → headlineMedium
//   editor-body (Literata 20/32 Regular)  → bodyLarge  ← primary editor style
//   ui-body-md  (Manrope  16/24 Regular)  → bodyMedium
//   label-lg    (Manrope  14/20 SemiBold) → labelLarge
//   label-sm    (Manrope  12/16 Medium)   → labelSmall

val EbookEditorTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = Literata,
        fontWeight = FontWeight.SemiBold,
        fontSize = 48.sp,
        lineHeight = 56.sp,
        letterSpacing = 0.sp,
    ),
    displayMedium = TextStyle(
        fontFamily = Literata,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp,
        lineHeight = 48.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = Literata,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    // Primary body style for the distraction-free editor surface
    bodyLarge = TextStyle(
        fontFamily = Literata,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.2.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)
