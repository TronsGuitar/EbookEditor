package com.tronsguitar.ebookeditor.domain.model

data class Paragraph(
    val id: Long = 0,
    val sectionId: Long,
    val text: String,
    val semanticStyle: SemanticTextStyle = SemanticTextStyle.BODY,
    val manualFormatting: ManualFormatting = ManualFormatting(),
    val orderIndex: Int = 0,
) {
    fun resolvedStyle(): SemanticTextStyle = semanticStyle
}

enum class SemanticTextStyle {
    CHAPTER_TITLE,
    SECTION_TITLE,
    BODY,
    QUOTE,
}

data class ManualFormatting(
    val requestedStyle: SemanticTextStyle? = null,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
)
