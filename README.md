# EbookEditor

The Digital Study is an Android-first ebook editor focused on professional publishing output and adaptive writing workflows.

## Tech Stack Summary

### Frontend
- **Platform:** Android (optimized for Foldables and Tablets).
- **Language/Framework:** Kotlin with Jetpack Compose for adaptive layouts (including Fold6-class devices).
- **Design System:** "Writer's Sanctuary" theme using:
  - **Newsreader** (Serif)
  - **Manrope** (Sans-serif)
  - Specialized Light and Dark modes

### Backend & Services
- **Manuscript Engine:** Custom parsing service for DOCX/PDF ingestion, transforming fixed-layout artifacts into semantic, reflowable HTML/EPUB structures.
- **AI Services:** LLM integrations for:
  - AI Insights (for character arc consistency)
  - Automated semantic analysis during import
- **Database:** Local-first data layer (likely Room) with cloud synchronization for manuscript CRUD and version history.

### Compliance & Export
- **Formatting Engine:** Targets EPUB 3.0 and D2D 2026 standards.
- **Quality Auditor:** Automated logic gate validating metadata, AI usage disclosures, and image resolution requirements before export.

This stack prioritizes professional typography and semantic integrity so manuscript output is ready for major publishing platforms.

## CI build delivery (zipped APK by email)

The `Build APK` workflow now zips the generated debug APK and emails it after successful builds (non-PR runs).

Configure these repository secrets:
- `SMTP_SERVER`
- `SMTP_PORT`
- `SMTP_USERNAME`
- `SMTP_PASSWORD`
- `SMTP_SECURE` (optional, defaults to `true`)
- `BUILD_EMAIL_TO`
- `BUILD_EMAIL_FROM`

If any are missing, the workflow will still build and upload the zip artifact but will skip email delivery.
