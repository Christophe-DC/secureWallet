
import androidx.compose.runtime.*
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import com.cdcoding.system.ui.theme.SecureWalletTheme
import com.cdcoding.welcomedi.welcomeScreenModule
import com.cdcoding.welcomeimpl.ui.WelcomeScreen


@Composable
fun App() {
    ScreenRegistry {
        welcomeScreenModule()
    }

    SecureWalletTheme {
        Navigator( screen =  WelcomeScreen())
    }
}