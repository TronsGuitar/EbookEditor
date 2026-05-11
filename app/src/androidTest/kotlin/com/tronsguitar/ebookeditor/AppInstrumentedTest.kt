package com.tronsguitar.ebookeditor

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test that validates the correct application package is installed.
 *
 * These tests run on an Android device or emulator.
 */
@RunWith(AndroidJUnit4::class)
class AppInstrumentedTest {

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.tronsguitar.ebookeditor", appContext.packageName)
    }
}
