---
name: Writer's Sanctuary Dark
colors:
  surface: '#111316'
  surface-dim: '#111316'
  surface-bright: '#37393c'
  surface-container-lowest: '#0c0e11'
  surface-container-low: '#1a1c1f'
  surface-container: '#1e2023'
  surface-container-high: '#282a2d'
  surface-container-highest: '#333538'
  on-surface: '#e2e2e6'
  on-surface-variant: '#d0c5af'
  inverse-surface: '#e2e2e6'
  inverse-on-surface: '#2f3034'
  outline: '#99907c'
  outline-variant: '#4d4635'
  surface-tint: '#e9c349'
  primary: '#f2ca50'
  on-primary: '#3c2f00'
  primary-container: '#d4af37'
  on-primary-container: '#554300'
  inverse-primary: '#735c00'
  secondary: '#c5c6cc'
  on-secondary: '#2e3035'
  secondary-container: '#47494e'
  on-secondary-container: '#b7b8be'
  tertiary: '#cfcecf'
  on-tertiary: '#303031'
  tertiary-container: '#b3b2b3'
  on-tertiary-container: '#444546'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#ffe088'
  primary-fixed-dim: '#e9c349'
  on-primary-fixed: '#241a00'
  on-primary-fixed-variant: '#574500'
  secondary-fixed: '#e2e2e8'
  secondary-fixed-dim: '#c5c6cc'
  on-secondary-fixed: '#191c20'
  on-secondary-fixed-variant: '#45474c'
  tertiary-fixed: '#e3e2e3'
  tertiary-fixed-dim: '#c7c6c7'
  on-tertiary-fixed: '#1b1c1d'
  on-tertiary-fixed-variant: '#464748'
  background: '#111316'
  on-background: '#e2e2e6'
  surface-variant: '#333538'
typography:
  headline-xl:
    fontFamily: Newsreader
    fontSize: 48px
    fontWeight: '600'
    lineHeight: '1.2'
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Newsreader
    fontSize: 32px
    fontWeight: '500'
    lineHeight: '1.3'
  headline-lg-mobile:
    fontFamily: Newsreader
    fontSize: 28px
    fontWeight: '500'
    lineHeight: '1.3'
  headline-md:
    fontFamily: Newsreader
    fontSize: 24px
    fontWeight: '500'
    lineHeight: '1.4'
  body-lg:
    fontFamily: Manrope
    fontSize: 18px
    fontWeight: '400'
    lineHeight: '1.7'
  body-md:
    fontFamily: Manrope
    fontSize: 16px
    fontWeight: '400'
    lineHeight: '1.6'
  label-md:
    fontFamily: Manrope
    fontSize: 14px
    fontWeight: '600'
    lineHeight: '1.2'
    letterSpacing: 0.05em
  label-sm:
    fontFamily: Manrope
    fontSize: 12px
    fontWeight: '500'
    lineHeight: '1.2'
    letterSpacing: 0.03em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  unit: 4px
  gutter: 24px
  margin-edge: 32px
  container-max: 1120px
  writing-well: 720px
---

## Brand & Style

The design system is a high-end, editorial environment crafted for deep immersion and intellectual rigor. It transitions the classic "Writer's Sanctuary" aesthetic into a nocturnal setting, prioritizing ocular comfort without sacrificing the prestige of a professional publishing tool. 

The style is **Minimalist-Corporate**, utilizing quiet depth and refined typography to create a "digital study." It avoids distracting vibrant glows in favor of muted, light-absorbent surfaces. The emotional response is one of solitude, focus, and executive-level craftsmanship.

## Colors

The palette is anchored by "Midnight Ink" (#121314) for the deepest canvases, providing a true void that allows text to breathe. Secondary surfaces use "Deep Charcoal" (#1a1c1e) to establish subtle physical boundaries.

The primary accent is a **Muted Gold**, used sparingly for intent-driven actions. This color provides a sophisticated warmth against the cool-toned grays. Text uses "Soft Bone" (#e2e2e6), a high-contrast off-white that prevents the "haloing" effect often caused by pure white text on black backgrounds, ensuring legibility during multi-hour writing sessions.

## Typography

This design system employs a sophisticated serif-sans pairing to balance literary tradition with modern utility. 

**Newsreader** serves as the editorial voice, used for headlines and titles to evoke the feeling of a prestige publication. It features slightly higher weights and generous line heights to maintain its elegant silhouette in dark mode. 

**Manrope** is the workhorse for the interface and body copy. Its geometric yet friendly proportions provide excellent clarity. Body text is set with an increased line height (1.7) to maximize readability and reduce eye fatigue in low-light environments.

## Layout & Spacing

The layout follows a **Fixed Grid** philosophy for the core writing experience, centering a "Writing Well" of 720px to prevent excessive eye scanning. 

For broader application views, a 12-column grid is used with 24px gutters. Spacing follows a 4px baseline shift, but emphasizes "Negative Space" as a functional element. Large margins (32px+) on the screen edges reinforce the sense of a sanctuary, pushing the content to the center of the user's focus. On mobile, margins reduce to 16px, and the layout reflows to a single column with increased vertical padding between editorial blocks.

## Elevation & Depth

In this dark-mode system, depth is communicated through **Tonal Layering** rather than traditional shadows. Shadows in a deep charcoal environment often feel muddy; instead, we elevate elements by shifting their background color to a lighter gray.

- **Level 0 (Base):** Midnight Blue (#121314) for the main application background.
- **Level 1 (Surface):** Deep Charcoal (#1a1c1e) for sidebars, cards, and toolbars.
- **Level 2 (Elevated):** Soft Slate (#25272a) for modals, dropdowns, and floating menus.

To define boundaries without adding visual noise, use a **Low-contrast outline** (1px solid #2c2e33) on all elevated surfaces. This creates a crisp, architectural feel.

## Shapes

The design system utilizes **ROUND_FOUR** (Rounded) geometry. This 0.5rem (8px) base radius provides a modern, accessible feel that softens the "technical" edge of the dark theme. It suggests a tool that is precise but welcoming. Larger components like main content containers or feature cards may scale up to 1.5rem (24px) to emphasize their container status within the layout.

## Components

### Buttons
Primary buttons use the **Muted Gold** background with dark text for maximum visibility. Secondary buttons are "ghost" style with a Soft Bone outline. All buttons use 8px rounded corners and Manrope Semibold.

### Cards
Cards are flat surfaces (#1a1c1e) with no shadow, defined by a 1px border (#2c2e33). They should appear integrated into the layout rather than floating above it.

### Input Fields
Fields use the deepest background (#121314) with a subtle border. Upon focus, the border transitions to the Muted Gold, providing a clear but elegant signal of activity.

### Chips & Tags
Small, pill-shaped elements with a secondary gray background. They use Manrope Medium at 12px for meta-information like "Draft," "Published," or "Word Count."

### The Writing Canvas
The central component of the system. It should be free of all chrome, using the Newsreader font for the title and Manrope for the body. The cursor should be a subtle Muted Gold line to act as a beacon for the writer’s eye.