# The Digital Study: eBook Creation & Publishing Platform

A professional-grade Android application optimized for large-screen devices (Fold6, tablets), designed to streamline the manuscript-to-eBook pipeline while adhering to Draft2Digital (D2D) 2026 content and layout standards.

## Project Vision
To provide authors with a "Writer's Sanctuary"—a focused, high-fidelity environment for drafting, importing, reformatting, and publishing manuscripts with semantic integrity and automated compliance checks.

## Core Features & Backend Requirements

### 1. Manuscript Management (Dashboard)
- **Feature:** A central hub for recent projects with visual cover previews and progress tracking.
- **Backend Needs:** 
  - CRUD operations for Project entities.
  - Progress calculation logic (word count vs. weekly goals).
  - AI Insight engine to flag character arc consistency or structural issues.

### 2. Smart Import & Reformatting
- **Feature:** Support for DOCX and PDF ingestion with automated semantic analysis.
- **Backend Needs:**
  - File upload handling and storage.
  - Parsing engine for DOCX/PDF to extract semantic structure (H1, H2, Body).
  - Tonal layering and normalization logic to convert fixed-layout artifacts into reflowable text.
  - Real-time logging of automated layout fixes (e.g., removing hard line breaks, standardizing headers).

### 3. Semantic eBook Editor
- **Feature:** A distraction-free Focus Mode editor that prioritizes semantic styles over manual formatting.
- **Backend Needs:**
  - Real-time text sync and auto-save.
  - Chapter/Section hierarchy management.
  - Metadata tracking for every chapter (word counts, status).

### 4. D2D Compliance & Publishing (Export)
- **Feature:** Export to Reflowable EPUB, Print-Ready PDF, and DOCX.
- **Backend Needs:**
  - Conversion engine targeting EPUB 3.0 standards.
  - Automated Quality Check auditor (validating AI disclosures, metadata validity, and image resolution).
  - Automated Endmatter generator (About the Author, 'Also By' pages) synced with author profile.

### 5. Project Settings & Metadata
- **Feature:** Comprehensive book metadata configuration including AI usage disclosures.
- **Backend Needs:**
  - Mandatory AI disclosure tracking for D2D compliance.
  - Real-time metadata validator (keyword stuffing detection).
  - Cover art storage and bleed-edge validation.

## Technical Architecture

### Frontend
- **Platform:** Android (Optimized for Large Screens/Foldables).
- **Design System:** "Writer's Sanctuary" (Serif-focused typography, adaptable Light/Dark modes).
- **UI Patterns:** Navigation Drawer (Sidebar) for tablets, Bottom Navigation for mobile/compact views.

### Backend/API
- **Auth:** Individual author profiles.
- **Storage:** Secure manuscript storage with version history.
- **AI Services:** Integration for semantic analysis and quality auditing.

## Compliance Standards
All generated output must satisfy the following:
- **Layout:** Reflowable geometry based on the D2D Pocket Guide.
- **Content:** Adherence to D2D 2026 Content Guidelines (Anti-keyword stuffing, AI transparency).
