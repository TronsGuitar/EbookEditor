package com.tronsguitar.ebookeditor

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.tronsguitar.ebookeditor.auth.DeviceAuthAvailability
import com.tronsguitar.ebookeditor.auth.mapDeviceAuthAvailability
import com.tronsguitar.ebookeditor.auth.requiredAuthenticators
import com.tronsguitar.ebookeditor.ui.navigation.AppNavigation
import com.tronsguitar.ebookeditor.ui.theme.EbookEditorTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The single entry-point Activity for The Digital Study.
 *
 * Edge-to-edge rendering is enabled so the app can draw behind system bars,
 * which is important for the full-bleed editor surface on tablets and foldables.
 */
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EbookEditorTheme {
                AuthenticatedContent(
                    onAuthenticate = { onSuccess, onError ->
                        authenticateWithDevice(onSuccess = onSuccess, onError = onError)
                    },
                )
            }
        }
    }

    private fun authenticateWithDevice(
        onSuccess: () -> Unit,
        onError: (DeviceAuthAvailability) -> Unit,
    ) {
        val availability = mapDeviceAuthAvailability(
            BiometricManager.from(this).canAuthenticate(requiredAuthenticators()),
        )
        if (availability != DeviceAuthAvailability.AVAILABLE) {
            onError(availability)
            return
        }

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError(DeviceAuthAvailability.UNKNOWN)
            }
        }

        val prompt = BiometricPrompt(this, mainExecutor, callback)
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.auth_prompt_title))
            .setSubtitle(getString(R.string.auth_prompt_subtitle))
            .setAllowedAuthenticators(requiredAuthenticators())
            .build()
        prompt.authenticate(promptInfo)
    }
}

@Composable
private fun AuthenticatedContent(
    onAuthenticate: (onSuccess: () -> Unit, onError: (DeviceAuthAvailability) -> Unit) -> Unit,
) {
    var isAuthenticated by rememberSaveable { mutableStateOf(false) }
    var availabilityError by rememberSaveable { mutableStateOf<DeviceAuthAvailability?>(null) }

    if (isAuthenticated) {
        AppNavigation()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.auth_gate_title),
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = stringResource(R.string.auth_gate_message),
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
        )
        Button(
            onClick = {
                availabilityError = null
                onAuthenticate(
                    { isAuthenticated = true },
                    { availabilityError = it },
                )
            },
        ) {
            Text(text = stringResource(R.string.auth_gate_button))
        }
        availabilityError?.let { error ->
            Text(
                text = when (error) {
                    DeviceAuthAvailability.NO_HARDWARE ->
                        stringResource(R.string.auth_error_no_hardware)
                    DeviceAuthAvailability.HARDWARE_UNAVAILABLE ->
                        stringResource(R.string.auth_error_hardware_unavailable)
                    DeviceAuthAvailability.NONE_ENROLLED ->
                        stringResource(R.string.auth_error_none_enrolled)
                    DeviceAuthAvailability.UNKNOWN ->
                        stringResource(R.string.auth_error_generic)
                    DeviceAuthAvailability.AVAILABLE -> ""
                },
                modifier = Modifier.padding(top = 12.dp),
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}
