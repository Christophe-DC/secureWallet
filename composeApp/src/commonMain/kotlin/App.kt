
import androidx.compose.runtime.*
import com.cdcoding.system.ui.theme.SecureWalletTheme
import com.cdcoding.welcomeimpl.ui.WelcomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    SecureWalletTheme {
        WelcomeScreen()
    }
}