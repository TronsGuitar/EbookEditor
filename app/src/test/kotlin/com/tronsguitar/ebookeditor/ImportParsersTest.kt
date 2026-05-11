package com.tronsguitar.ebookeditor

import com.tronsguitar.ebookeditor.ui.screens.importmanuscript.extractDocxText
import com.tronsguitar.ebookeditor.ui.screens.importmanuscript.extractPdfTextFromBytes
import com.tronsguitar.ebookeditor.ui.screens.importmanuscript.extractPdfTextFromStream
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ImportParsersTest {

    @Test
    fun `extractDocxText returns paragraph text from document xml`() {
        val docxBytes = buildDocx(
            """
            <w:document>
              <w:body>
                <w:p><w:r><w:t>Hello</w:t></w:r></w:p>
                <w:p><w:r><w:t>World</w:t></w:r></w:p>
              </w:body>
            </w:document>
            """.trimIndent(),
        )

        val extracted = extractDocxText(ByteArrayInputStream(docxBytes))

        assertEquals("Hello\nWorld", extracted)
    }

    @Test
    fun `extractDocxText decodes common xml entities and normalizes whitespace`() {
        val docxBytes = buildDocx(
            """
            <w:document>
              <w:body>
                <w:p><w:r><w:t> Rock &amp; Roll </w:t></w:r></w:p>
                <w:p><w:r><w:t>&lt;Intro&gt;</w:t></w:r></w:p>
              </w:body>
            </w:document>
            """.trimIndent(),
        )

        val extracted = extractDocxText(ByteArrayInputStream(docxBytes))

        assertEquals("Rock & Roll\n<Intro>", extracted)
    }

    @Test
    fun `extractDocxText returns empty string when document xml entry is missing`() {
        val zipWithoutDocumentXml = buildZip(
            "word/styles.xml" to "<w:styles/>",
            "[Content_Types].xml" to "<Types/>",
        )

        val extracted = extractDocxText(ByteArrayInputStream(zipWithoutDocumentXml))

        assertEquals("", extracted)
    }

    @Test
    fun `extractDocxText finds document xml after unrelated zip entries`() {
        val docxBytes = buildZip(
            "word/styles.xml" to "<w:styles/>",
            "word/document.xml" to "<w:document><w:body><w:p><w:r><w:t>Late</w:t></w:r></w:p></w:body></w:document>",
        )

        val extracted = extractDocxText(ByteArrayInputStream(docxBytes))

        assertEquals("Late", extracted)
    }

    @Test
    fun `extractPdfText extracts simple text operators`() {
        val pdfContent = "BT (Hello PDF) Tj ET".toByteArray(Charsets.ISO_8859_1)

        val extracted = extractPdfTextFromBytes(pdfContent)

        assertEquals("Hello PDF", extracted)
    }

    @Test
    fun `extractPdfText extracts multiple text operators in order`() {
        val pdfContent = "BT (Line One) Tj ET BT (Line Two) Tj ET".toByteArray(Charsets.ISO_8859_1)

        val extracted = extractPdfTextFromBytes(pdfContent)

        assertEquals("Line One\nLine Two", extracted)
    }

    @Test
    fun `extractPdfText limits operator capture and falls back for oversized operator text`() {
        val oversized = "a".repeat(501)
        val pdfContent = "BT ($oversized) Tj ET".toByteArray(Charsets.ISO_8859_1)

        val extracted = extractPdfTextFromBytes(pdfContent)

        assertTrue(extracted.contains("BT ("))
        assertFalse(extracted.contains("\n"))
    }

    @Test
    fun `extractPdfText falls back to printable ascii when no operators found`() {
        val binaryLikeContent = "manuscript preview body".toByteArray(Charsets.ISO_8859_1)

        val extracted = extractPdfTextFromBytes(binaryLikeContent)

        assertTrue(extracted.contains("manuscript preview body"))
    }

    @Test
    fun `extractPdfText fallback trims to max length`() {
        val veryLongAscii = "x".repeat(4500).toByteArray(Charsets.ISO_8859_1)

        val extracted = extractPdfTextFromBytes(veryLongAscii)

        assertEquals(4000, extracted.length)
    }

    @Test
    fun `extractPdfTextFromStream reads at most one megabyte`() {
        val content = ByteArray(1_048_576 + 128) { 'a'.code.toByte() }
        val marker = "(TRAILING_MARKER) Tj".toByteArray(Charsets.ISO_8859_1)
        marker.copyInto(content, destinationOffset = 1_048_576)

        val extracted = extractPdfTextFromStream(ByteArrayInputStream(content))

        assertFalse(extracted.contains("TRAILING_MARKER"))
    }

    private fun buildDocx(documentXml: String): ByteArray {
        return buildZip("word/document.xml" to documentXml)
    }

    private fun buildZip(vararg entries: Pair<String, String>): ByteArray {
        val output = ByteArrayOutputStream()
        ZipOutputStream(output).use { zip ->
            entries.forEach { (name, value) ->
                zip.putNextEntry(ZipEntry(name))
                zip.write(value.toByteArray())
                zip.closeEntry()
            }
        }
        return output.toByteArray()
    }
}
