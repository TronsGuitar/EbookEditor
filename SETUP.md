## Getting Started

Follow these steps to set up the development environment for building EbookEditor on Windows.

### Prerequisites

- **Windows 10/11** (64-bit)
- **Android Studio Ladybug (2024.2.1) or newer**
  - Download: https://developer.android.com/studio
  - Includes: JDK 17, Android SDK, Gradle wrapper support
- **Android SDK** with the following components (installed via Android Studio SDK Manager):
  - **SDK Platform** – Android 15 (API 35) ← compile target
  - **SDK Platform** – Android 8.0 (API 26) ← minimum supported
  - **Android Emulator** (optional – for running without a physical device)
  - **Google USB Driver** (optional – for physical device debugging)

### Clone and Open

```powershell
git clone https://github.com/TronsGuitar/EbookEditor.git
cd EbookEditor
```

Then open Android Studio → **File → Open** → select the `EbookEditor` folder.
Android Studio will automatically:
1. Detect `settings.gradle.kts`.
2. Download the Gradle 8.9 distribution (defined in `gradle/wrapper/gradle-wrapper.properties`).
3. Sync all dependencies from Maven Central / Google Maven.

> **Note:** The `gradle-wrapper.jar` binary is required for command-line builds.
> If you need to run `gradlew.bat` outside Android Studio, generate the wrapper by running:
> ```powershell
> gradle wrapper --gradle-version=8.9
> ```
> (Requires a local Gradle 8.x installation.)

### Configure local.properties

Create a file named `local.properties` in the project root (it is git-ignored):

```properties
sdk.dir=C:\\Users\\<YourUsername>\\AppData\\Local\\Android\\Sdk
```

Android Studio creates this automatically when you first sync the project.

### Build & Run

| Task | Command (PowerShell) |
|------|----------------------|
| Debug build | `.\gradlew.bat assembleDebug` |
| Run unit tests | `.\gradlew.bat test` |
| Install on device | `.\gradlew.bat installDebug` |
| Release build | `.\gradlew.bat assembleRelease` |

### Add Custom Fonts

The "Writer's Sanctuary" design system uses two custom typefaces. Download them from
Google Fonts and place the `.ttf` files in `app/src/main/res/font/`:

| Font | URL | Files needed |
|------|-----|--------------|
| Literata | https://fonts.google.com/specimen/Literata | `literata_regular.ttf`, `literata_medium.ttf`, `literata_semibold.ttf`, `literata_bold.ttf`, `literata_italic.ttf` |
| Manrope  | https://fonts.google.com/specimen/Manrope  | `manrope_regular.ttf`, `manrope_medium.ttf`, `manrope_semibold.ttf`, `manrope_bold.ttf` |

After adding the files, update `app/src/main/kotlin/…/ui/theme/Type.kt` to use
`FontFamily(Font(R.font.literata_regular, …), …)` as described in the comments there.
