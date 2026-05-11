package com.tronsguitar.ebookeditor

import com.tronsguitar.ebookeditor.ui.screens.importmanuscript.extractDocxText
import com.tronsguitar.ebookeditor.ui.screens.importmanuscript.extractPdfText
import org.junit.Assert.assertEquals
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
    fun `extractPdfText extracts simple text operators`() {
        val pdfContent = "BT (Hello PDF) Tj ET".toByteArray(Charsets.ISO_8859_1)

        val extracted = extractPdfText(pdfContent)

        assertEquals("Hello PDF", extracted)
    }

    @Test
    fun `extractPdfText falls back to printable ascii when no operators found`() {
        val binaryLikeContent = "manuscript preview body".toByteArray(Charsets.ISO_8859_1)

        val extracted = extractPdfText(binaryLikeContent)

        assertTrue(extracted.contains("manuscript preview body"))
    }

    private fun buildDocx(documentXml: String): ByteArray {
        val output = ByteArrayOutputStream()
        ZipOutputStream(output).use { zip ->
            zip.putNextEntry(ZipEntry("word/document.xml"))
            zip.write(documentXml.toByteArray())
            zip.closeEntry()
        }
        return output.toByteArray()
    }
}
