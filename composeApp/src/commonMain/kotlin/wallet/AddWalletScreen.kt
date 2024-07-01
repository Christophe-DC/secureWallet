package wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun AddWalletScreen(modifier: Modifier = Modifier) {
    var password by remember { mutableStateOf("") }
    var walletCreatedMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                walletCreatedMessage = createWallet(password)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Wallet")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (walletCreatedMessage.isNotEmpty()) {
            Text(walletCreatedMessage)
        }
    }
}

fun createWallet(password: String): String {
    // Logic to create wallet and return a message
    // For example purposes, we'll just return a success message
    return "Wallet created successfully!"
}

fun generateSeedPhrase(): String {
    // Generate initial entropy
   /* val initialEntropy = ByteArray(16) // 128 bits of entropy for a 12-word mnemonic
    SecureRandom().nextBytes(initialEntropy)

    // Convert entropy to mnemonic words
    val mnemonicCode = MnemonicCode.INSTANCE
    val mnemonicWords = mnemonicCode.toMnemonic(initialEntropy)
    return mnemonicWords.joinToString(" ")*/
    return ""
}