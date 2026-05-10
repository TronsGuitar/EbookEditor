---
name: Writer's Sanctuary
colors:
  surface: '#fbf9f4'
  surface-dim: '#dbdad5'
  surface-bright: '#fbf9f4'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f5f3ee'
  surface-container: '#f0eee9'
  surface-container-high: '#eae8e3'
  surface-container-highest: '#e4e2dd'
  on-surface: '#1b1c19'
  on-surface-variant: '#44474a'
  inverse-surface: '#30312e'
  inverse-on-surface: '#f2f1ec'
  outline: '#75777b'
  outline-variant: '#c5c6cb'
  surface-tint: '#595f66'
  primary: '#181e24'
  on-primary: '#ffffff'
  primary-container: '#2d3339'
  on-primary-container: '#959ba2'
  inverse-primary: '#c1c7cf'
  secondary: '#545f72'
  on-secondary: '#ffffff'
  secondary-container: '#d5e0f7'
  on-secondary-container: '#586377'
  tertiary: '#0e1e30'
  on-tertiary: '#ffffff'
  tertiary-container: '#243346'
  on-tertiary-container: '#8c9bb2'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#dde3eb'
  primary-fixed-dim: '#c1c7cf'
  on-primary-fixed: '#161c22'
  on-primary-fixed-variant: '#41474e'
  secondary-fixed: '#d8e3fa'
  secondary-fixed-dim: '#bcc7dd'
  on-secondary-fixed: '#111c2c'
  on-secondary-fixed-variant: '#3c475a'
  tertiary-fixed: '#d4e4fc'
  tertiary-fixed-dim: '#b8c8e0'
  on-tertiary-fixed: '#0d1c2e'
  on-tertiary-fixed-variant: '#39485c'
  background: '#fbf9f4'
  on-background: '#1b1c19'
  surface-variant: '#e4e2dd'
typography:
  display-lg:
    fontFamily: Literata
    fontSize: 48px
    fontWeight: '600'
    lineHeight: 56px
  headline-lg:
    fontFamily: Manrope
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
  headline-md:
    fontFamily: Manrope
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  editor-body:
    fontFamily: Literata
    fontSize: 20px
    fontWeight: '400'
    lineHeight: 32px
    letterSpacing: 0.2px
  ui-body-md:
    fontFamily: Manrope
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-lg:
    fontFamily: Manrope
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.1px
  label-sm:
    fontFamily: Manrope
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
rounded:
  sm: 0.125rem
  DEFAULT: 0.25rem
  md: 0.375rem
  lg: 0.5rem
  xl: 0.75rem
  full: 9999px
spacing:
  margin-side: 24px
  gutter: 16px
  sidebar-width: 280px
  editor-max-width: 800px
  unit-base: 8px
---

## Brand & Style

The design system is centered on the concept of "The Digital Study"—a quiet, high-performance environment that bridges the gap between traditional tactile publishing and modern software efficiency. The personality is intellectual, reliable, and unobtrusive, designed specifically for authors and editors who spend hours within the interface.

The visual style follows a **Modern-Minimalist** approach with a heavy emphasis on readability and cognitive ease. It leverages Material Design 3 (M3) structural logic but strips away excessive ornamentation and vibrant hues in favor of a "paper-and-ink" digital philosophy. This ensures that the user’s content remains the focal point, while the UI recedes into a supportive, functional background.

## Colors

This design system utilizes a low-strain, sophisticated palette designed for long-form focus. 

- **Primary & Secondary:** Deep charcoal (#2D3339) and slate blues form the core of the UI framework, providing high-contrast legibility for navigation and controls without the harshness of pure black.
- **Neutral / Surface:** Instead of stark white, a warm "Paper-White" (#F9F7F2) is used for the primary workspace to significantly reduce blue-light eye strain.
- **Backgrounds:** The primary app background uses a slightly darker tint than the editor surface to create a clear "desk vs. document" hierarchy.
- **Status & Feedback:** Functional colors (success, error) should be desaturated to maintain the professional, understated aesthetic.

## Typography

The typography system uses a dual-font strategy to separate content creation from software orchestration.

- **The Editor (Literata):** A high-quality, bookish serif designed specifically for digital reading and writing. It features generous line heights and subtle character distinctions to prevent fatigue during intense editing sessions.
- **The UI (Manrope):** A modern, geometric sans-serif that provides a crisp, functional contrast to the editor. It is used for menus, sidebars, and metadata.
- **Hierarchy:** Headlines for the document use Literata to preview the "final book" feel, while UI headers use Manrope to maintain a systematic, professional app feel.

## Layout & Spacing

This design system is optimized for large-screen Android devices (tablets and foldables) using a **Multi-Pane Adaptive Grid**.

- **Structure:** A permanent or persistent Navigation Rail/Drawer on the left, a "List" pane for chapter management, and a central "Detail" pane for the editor.
- **The Golden Ratio Column:** In the editor view, the text container is capped at 800px regardless of screen width. This maintains the "optimal line length" (60–80 characters) essential for professional editing.
- **Multi-Window Support:** Elements must reflow from a 3-pane layout (Navigation + Chapter List + Editor) to a 1-pane layout (Editor only) seamlessly using M3 canonical layouts.
- **Spacing Rhythm:** A strict 8dp grid governs all padding and margins to ensure a disciplined, structured appearance.

## Elevation & Depth

This design system adopts **Tonal Layering** over heavy shadows. Depth is communicated through subtle color shifts rather than dramatic lighting effects to keep the UI flat and non-distracting.

- **Level 0 (Surface):** The main app background (Slate-tinted neutral).
- **Level 1 (Container):** Sidebars and secondary panes, slightly lighter or darker than the background.
- **Level 2 (Active Editor):** The document itself, using the warmest "Paper-White" to draw the eye.
- **Overlays:** Modals and menus use a very soft, diffused shadow (12% opacity, 16px blur) with a subtle 1px border (#E2E8F0) to define boundaries without adding visual "weight."

## Shapes

The shape language is **Soft and Precise**. A 4dp (0.25rem) base radius is applied to most UI components to convey a professional, slightly architectural feel. 

- **Small Components:** Buttons and input fields use the base 4dp radius.
- **Medium Components:** Cards and side-sheets use an 8dp (0.5rem) radius.
- **Large Components:** Dialogs and bottom sheets use a 12dp (0.75rem) radius.
- **Exception:** Floating Action Buttons (FABs) follow M3 standards but use a rounded-square shape rather than a circle to align with the system's geometric precision.

## Components

Components are refined versions of Material Design 3 elements, tailored for a productivity-first context.

- **Buttons:** Primarily "Filled" for main actions (Publish, Save) and "Outlined" for secondary actions. We avoid "Tonal" buttons to keep the interface high-contrast.
- **The "Focus Drawer":** A collapsible right-hand sidebar for metadata, comments, and character notes, using a tonal surface that matches the chapter list.
- **Input Fields:** Filled fields with a subtle bottom-stroke only. The fill color should be just 2% darker than the surface it sits on to remain minimalist.
- **Chips:** Used for "Tags" or "Genres," these should be rectangular with the Soft radius (4dp) and minimal padding to look like classic library labels.
- **Chapter List:** High-density list items with clear active-state indicators (a 4px vertical bar on the left) rather than full-row highlighting, to minimize visual noise.
- **Contextual Toolbars:** A floating formatting bar that appears only on text selection, using a dark charcoal background to pop against the warm paper-white editor.