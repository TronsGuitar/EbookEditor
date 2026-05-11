package com.tronsguitar.ebookeditor.auth

import androidx.biometric.BiometricManager
import org.junit.Assert.assertEquals
import org.junit.Test

class DeviceAuthenticationTest {

    @Test
    fun `maps biometric status codes to availability states`() {
        assertEquals(
            DeviceAuthAvailability.AVAILABLE,
            mapDeviceAuthAvailability(BiometricManager.BIOMETRIC_SUCCESS),
        )
        assertEquals(
            DeviceAuthAvailability.NO_HARDWARE,
            mapDeviceAuthAvailability(BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE),
        )
        assertEquals(
            DeviceAuthAvailability.HARDWARE_UNAVAILABLE,
            mapDeviceAuthAvailability(BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE),
        )
        assertEquals(
            DeviceAuthAvailability.NONE_ENROLLED,
            mapDeviceAuthAvailability(BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED),
        )
    }
}
