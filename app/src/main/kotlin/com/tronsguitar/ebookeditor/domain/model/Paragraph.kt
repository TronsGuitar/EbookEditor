package com.tronsguitar.ebookeditor.domain.model

data class Paragraph(
    val id: Long = 0,
    val sectionId: Long,
    val text: String,
    val semanticStyle: SemanticTextStyle = SemanticTextStyle.BODY,
    val manualFormatting: ManualFormatting = ManualFormatting(),
    val orderIndex: Int = 0,
) {
    /**
     * Semantic styles are authoritative for rendering and export, even when
     * manual style requests are present in [manualFormatting].
     */
    fun resolvedStyle(): SemanticTextStyle = semanticStyle
}

enum class SemanticTextStyle {
    CHAPTER_TITLE,
    SECTION_TITLE,
    BODY,
    QUOTE,
}

data class ManualFormatting(
    /**
     * Requested ad-hoc style captured from editor interactions.
     *
     * The semantic model keeps this value for auditing/editor UX, but it does
     * not override [Paragraph.semanticStyle].
     */
    val requestedStyle: SemanticTextStyle? = null,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
)
