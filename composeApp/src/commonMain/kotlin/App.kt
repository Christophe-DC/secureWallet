import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import com.cdcoding.system.ui.theme.SecureWalletTheme
import com.cdcoding.welcomedi.welcomeScreenModule
import com.cdcoding.welcomeimpl.ui.WelcomeScreen


@Composable
fun App(
    darkTheme: Boolean,
    dynamicColor: Boolean
) {
    ScreenRegistry {
        welcomeScreenModule()
    }

    SecureWalletTheme(darkTheme, dynamicColor) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Navigator(screen = WelcomeScreen())
        }
    }
}