package com.tronsguitar.ebookeditor.auth

import androidx.biometric.BiometricManager

private const val AUTHENTICATORS =
    BiometricManager.Authenticators.BIOMETRIC_STRONG or
        BiometricManager.Authenticators.DEVICE_CREDENTIAL

enum class DeviceAuthAvailability {
    AVAILABLE,
    NO_HARDWARE,
    HARDWARE_UNAVAILABLE,
    NONE_ENROLLED,
    UNKNOWN,
}

fun requiredAuthenticators(): Int = AUTHENTICATORS

fun mapDeviceAuthAvailability(status: Int): DeviceAuthAvailability = when (status) {
    BiometricManager.BIOMETRIC_SUCCESS -> DeviceAuthAvailability.AVAILABLE
    BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> DeviceAuthAvailability.NO_HARDWARE
    BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> DeviceAuthAvailability.HARDWARE_UNAVAILABLE
    BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> DeviceAuthAvailability.NONE_ENROLLED
    else -> DeviceAuthAvailability.UNKNOWN
}
