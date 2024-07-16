package com.philimonnag.smarttaskmanager.ui.screen


import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController


//val LocalActivity = compositionLocalOf<FragmentActivity> { error("error") }
@Composable
fun BiometricAuthScreen(onLoginSuccess:()->Unit) {
    var  biometricError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val biometricManager = BiometricManager.from(context)
    val isBiometricAvailable = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) ==
            BiometricManager.BIOMETRIC_SUCCESS

    LaunchedEffect(isBiometricAvailable) {
        if (isBiometricAvailable) {
            authenticateWithBiometrics(context, onLoginSuccess, { error ->
                biometricError = error
            })
        } else {
            biometricError = "Biometric authentication is not available on this device."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (biometricError != null) {
            Text(
                text = biometricError ?: "",
                color = Color.Red,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Button(
            onClick = {
                if (isBiometricAvailable) {
                    authenticateWithBiometrics(context, onLoginSuccess, { error ->
                        biometricError = error
                    })
                } else {
                    biometricError = "Biometric authentication is not available on this device."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Authenticate")
        }
    }
}

fun authenticateWithBiometrics(context: Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
    val executor = ContextCompat.getMainExecutor(context)
    val biometricPrompt = BiometricPrompt(context as androidx.fragment.app.FragmentActivity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                onError(errString.toString())
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Login")
        .setSubtitle("Place your finger on the sensor")
        .setNegativeButtonText("Cancel")
        .build()

    biometricPrompt.authenticate(promptInfo)

}

