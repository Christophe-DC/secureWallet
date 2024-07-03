package com.cdcoding.securewallet

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.SystemBarStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cdcoding.system.ui.theme.SecureWalletTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import androidx.compose.material3.Surface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SecureWalletTheme {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.light(
                        MaterialTheme.colorScheme.background.toArgb(),
                        MaterialTheme.colorScheme.background.toArgb()
                    ),
                    navigationBarStyle = SystemBarStyle.light(
                        MaterialTheme.colorScheme.background.toArgb(),
                        MaterialTheme.colorScheme.background.toArgb()
                    )
                )
               // WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
               Surface(Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
                   App()
               }
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}