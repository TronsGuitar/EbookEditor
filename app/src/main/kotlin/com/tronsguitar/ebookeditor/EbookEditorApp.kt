package com.tronsguitar.ebookeditor

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for EbookEditor (The Digital Study).
 *
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection
 * throughout the application.
 */
@HiltAndroidApp
class EbookEditorApp : Application()
