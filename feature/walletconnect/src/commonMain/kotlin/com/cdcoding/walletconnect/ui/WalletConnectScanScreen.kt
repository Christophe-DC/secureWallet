package com.cdcoding.walletconnect.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cdcoding.common.utils.getIconUrl
import com.cdcoding.core.designsystem.components.QrCodeRequest
import com.cdcoding.core.designsystem.components.Scene
import com.cdcoding.core.designsystem.hooks.useEffect
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.hooks.useScope
import com.cdcoding.core.designsystem.hooks.useSnackbar
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.model.Chain
import com.cdcoding.walletconnect.presentation.WalletConnectEvent
import com.cdcoding.walletconnect.presentation.WalletConnectIntent
import com.cdcoding.walletconnect.presentation.WalletConnectScreenMode
import com.cdcoding.walletconnect.presentation.WalletConnectUIState
import com.cdcoding.walletconnect.presentation.WalletConnectViewModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WalletConnectScanScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: WalletConnectViewModel = useInject()
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        val snackbar = useSnackbar()
        val scope = useScope()

        useEffect(true) {
            viewModel.effect.collectLatest { eff ->
                when (eff) {
                    is WalletConnectEvent.ShowToast ->
                        scope.launch { snackbar.showSnackbar(eff.message) }

                    WalletConnectEvent.NavigateBack ->
                        navigator.pop()
                }
            }
        }

        Scene(
            title = "Scan WalletConnect",
            onClose = { navigator.pop() },
            snackbar = snackbar
        ) {
            WalletConnectScanContent(
                state = state,
                onIntent = viewModel::setIntent
            )
        }
    }
}

@Composable
private fun WalletConnectScanContent(
    state: WalletConnectUIState,
    onIntent: (WalletConnectIntent) -> Unit
) {
    when (state.mode) {
        WalletConnectScreenMode.Idle,
        WalletConnectScreenMode.Error,
                WalletConnectScreenMode.Scanning -> {
            ScanContent(
                onResult = { uri -> onIntent(WalletConnectIntent.OnQrScanned(uri)) },
                onCancel = { onIntent(WalletConnectIntent.OnScanCanceled) }
            )
        }


        WalletConnectScreenMode.Pairing -> {
            LoadingContent(
                title = "Connexion…",
                subtitle = "Pairing WalletConnect en cours"
            )
        }

        WalletConnectScreenMode.WaitingProposal -> {
            LoadingContent(
                title = "En attente…",
                subtitle = "Ouvre ton wallet (ex: Uniswap) pour confirmer"
            )
        }

        WalletConnectScreenMode.Confirming -> {
            val proposal = state.proposal
            if (proposal == null) {
                LoadingContent(title = "Confirming…", subtitle = "Waiting for proposal")
            } else {
                WalletConnectConfirmContent(
                    dappName = proposal.dapp.name,
                    dappUrl = proposal.dapp.url,
                    networksToConnect = state.networksToConnect,
                    walletAccounts = state.walletAccounts,
                    onCancel = { onIntent(WalletConnectIntent.OnConfirmReject) },
                    onConnect = { onIntent(WalletConnectIntent.OnConfirmApprove) },
                )
            }
        }

        WalletConnectScreenMode.Connected -> {
            ConnectedContent(
                topic = state.sessionTopic,
                onDisconnect = { onIntent(WalletConnectIntent.OnDisconnect) }
            )
        }
    }

}

@Composable
private fun ScanContent(
    onResult: (String) -> Unit,
    onCancel: () -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        QrCodeRequest(
            onResult = onResult,
            onCanceled = onCancel
        )
    }
}

@Composable
private fun LoadingContent(title: String, subtitle: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(16.dp))
        Text(title, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(6.dp))
        Text(subtitle, style = MaterialTheme.typography.bodyMedium)
    }
}


@Composable
private fun ConnectedContent(
    topic: String?,
    onDisconnect: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Text("Connected ✅", style = MaterialTheme.typography.titleMedium)
        if (!topic.isNullOrBlank()) {
            Spacer(Modifier.height(8.dp))
            Text("Topic: $topic", style = MaterialTheme.typography.bodySmall)
        }
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = onDisconnect) {
            Text("Disconnect")
        }
    }
}



@Composable
private fun WalletConnectConfirmContent(
    dappName: String,
    dappUrl: String?,
    networksToConnect: List<String>,
    walletAccounts: List<com.cdcoding.model.Account>,
    onCancel: () -> Unit,
    onConnect: () -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {

        Spacer(Modifier.height(12.dp))

        // ✅ Header (center)
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("DApp connected", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(16.dp))


            Spacer(Modifier.height(12.dp))
            Text(
                text = "$dappName wants to connect to your wallet",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (!dappUrl.isNullOrBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = dappUrl,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

        }

        Spacer(Modifier.height(20.dp))

        // ✅ Networks list
        Text(
            text = "Networks",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(10.dp))

        val rows = remember(networksToConnect, walletAccounts) {
            networksToConnect
                .distinct()
                .map { chain ->
                    val label = chainLabel(chain)
                    val address = addressForChain(chain, walletAccounts)
                    val iconUrl = iconUrlForChain(chain)

                    NetworkRowUi(
                        chain = chain,
                        label = label,
                        address = address,
                        iconUrl = iconUrl
                    )
                }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (rows.isEmpty()) {
                item {
                    Text(
                        "No networks requested",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(
                    items = rows,
                    key = { it.chain }
                ) { row ->
                    NetworkCard(row)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Column(Modifier.padding(horizontal = 16.dp)) {
            Text("Permissions", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))
            Text("• View your wallet balance and activity", style = MaterialTheme.typography.bodyMedium)
            Text("• Request transaction approvals", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onCancel
            ) { Text("Cancel") }

            Button(
                modifier = Modifier.weight(1f),
                onClick = onConnect
            ) { Text("Connect") }
        }

        Spacer(Modifier.height(12.dp))
    }
}

private data class NetworkRowUi(
    val chain: String,
    val label: String,
    val address: String?,
    val iconUrl: String?
)

@Composable
private fun NetworkCard(row: NetworkRowUi) {
    androidx.compose.material3.Surface(
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if(!row.iconUrl.isNullOrEmpty()) {
                    KamelImage(
                        resource = asyncPainterResource(data = Url(row.iconUrl)),
                        contentDescription = row.label.take(1),
                        modifier = Modifier.size(20.dp),
                    )
                } else {
                    Text(row.label.take(1))
                }
            }

            Spacer(Modifier.height(0.dp).then(Modifier).width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(row.label, style = MaterialTheme.typography.titleMedium)
                if (!row.address.isNullOrBlank()) {
                    Text(shortAddress(row.address), style = MaterialTheme.typography.bodyMedium)
                } else {
                    Text("—", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

private fun chainLabel(chain: String): String {
    // Ex: "eip155:1", "eip155:42161", "solana:4sGj...", etc
    val parts = chain.split(":")
    if (parts.size < 2) return chain

    val ns = parts[0]
    val id = parts[1]

    return when (ns) {
        "eip155" -> when (id) {
            "1" -> "Ethereum"
            "56" -> "BNB Smart Chain"
            "137" -> "Polygon"
            "10" -> "Optimism"
            "42161" -> "Arbitrum"
            "8453" -> "Base"
            "324" -> "zkSync Era"
            "59144" -> "Linea"
            "204" -> "opBNB"
            else -> "EVM ($id)"
        }
        "solana" -> "Solana"
        "cosmos" -> "Cosmos"
        "tron" -> "Tron"
        "ton" -> "TON"
        else -> "${ns.uppercase()} ($id)"
    }
}

private fun iconUrlForChain(chainId: String): String? {
    return when (chainId.lowercase()) {
        "eip155:1" -> Chain.Ethereum.getIconUrl()
        "eip155:56" -> Chain.SmartChain.getIconUrl()
        "eip155:137" -> Chain.Polygon.getIconUrl()
        "eip155:42161" -> Chain.Arbitrum.getIconUrl()
        "eip155:10" -> Chain.Optimism.getIconUrl()
        "eip155:8453" -> Chain.Base.getIconUrl()
        "eip155:59144" -> Chain.Linea.getIconUrl()
        "eip155:324" -> Chain.ZkSync.getIconUrl()
        "eip155:204" -> Chain.OpBNB.getIconUrl()
        "solana:mainnet", "solana:4sgh..." -> Chain.Solana.getIconUrl()
        else -> null
    }
}


private fun addressForChain(
    chain: String,
    walletAccounts: List<com.cdcoding.model.Account>
): String? {
    val parts = chain.split(":")
    if (parts.size < 2) return null

    val ns = parts[0]

    return when (ns) {
        "eip155" -> walletAccounts
            .firstOrNull { it.address.startsWith("0x") }
            ?.address

        "solana" -> walletAccounts
            .firstOrNull { !it.address.startsWith("0x") && it.chain.name.equals("Solana", true) }
            ?.address

        else -> walletAccounts.firstOrNull()?.address
    }
}

private fun shortAddress(address: String, head: Int = 6, tail: Int = 4): String {
    if (address.length <= head + tail) return address
    return address.take(head) + "…" + address.takeLast(tail)
}