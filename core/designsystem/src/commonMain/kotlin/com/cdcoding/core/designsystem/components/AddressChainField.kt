package com.cdcoding.core.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cdcoding.core.designsystem.circularProgress.CircularProgressIndicator16
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.model.Chain
import com.cdcoding.model.NameRecord
import com.cdcoding.network.service.NameResolveService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Composable
fun AddressChainField(
    chain: Chain?,
    value: String,
    label: String,
    onValueChange: (String, NameRecord?) -> Unit,
    error: String = "",
    editable: Boolean = true,
    searchName: Boolean = true,
    onQrScanner: (() -> Unit)? = null,
) {
    val viewModel: AddressChainViewModel = useInject()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(key1 = value) {
        viewModel.onNameRecord(chain, value)
    }

    LaunchedEffect(key1 = uiState.nameRecord?.address) {
        onValueChange(uiState.nameRecord?.name ?: value, uiState.nameRecord)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    if (it.hasFocus) keyboardController?.show() else keyboardController?.hide()
                },
            value = value,
            singleLine = true,
            readOnly = !editable,
            label = { Text(label) },
            onValueChange = { newValue ->
                if (searchName) {
                    viewModel.onInput(newValue, chain)
                }
                onValueChange(newValue, uiState.nameRecord)
            },
            trailingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator16()
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                    if (uiState.isResolve) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Name is resolved",
                            tint = MaterialTheme.colorScheme.tertiary,
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                    if (uiState.isFail) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Default.Error,
                            contentDescription = "Name is fail",
                            tint = MaterialTheme.colorScheme.error,
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                    TransferTextFieldActions(
                        paste = {
                            onValueChange(
                                clipboardManager.getText()?.text ?: "",
                                uiState.nameRecord
                            )
                        },
                        qrScanner = onQrScanner
                    )
                }
            }
        )
        if (error.isNotEmpty()) {
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
fun TransferTextFieldActions(
    paste: (() -> Unit)? = null,
    qrScanner: (() -> Unit)? = null,
) {
    Row {
        if (paste != null) {
            IconButton(onClick = paste) {
                Icon(
                    imageVector = Icons.Default.ContentPaste,
                    contentDescription = "paste",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        if (qrScanner != null) {
            IconButton(onClick = qrScanner) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "scan_address",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

class AddressChainViewModel(
    private val nameResolveService: NameResolveService,
) : ViewModel() {

    private var nameResolveJob: Job? = null
    private val state = MutableStateFlow(State())
    val uiState = state.stateIn(viewModelScope, SharingStarted.Eagerly, State())

    private var resolveListener: ((NameRecord?) -> Unit)? = null

    fun onNameRecord(chain: Chain?, nameRecord: String) {
        if (nameRecord.isEmpty()) {
            state.update { State() }
            return
        }
        val current = state.value.nameRecord
        if (nameRecord != current?.name) {
            onInput(nameRecord, chain)
        }
    }

    fun onInput(input: String, chain: Chain?) {
        if (nameResolveJob?.isActive == true) {
            nameResolveJob?.cancel()
        }
        state.update { State() }
        if (chain == null) {
            return
        }
        val subdomains = input.split(".")
        if (subdomains.size <= 1 || subdomains.lastOrNull().isNullOrEmpty()) {
            return
        }
        state.update { State(isLoading = true) }
        nameResolveJob = viewModelScope.launch(Dispatchers.IO) {
            delay(500L)
            val nameRecord = nameResolveService.resolve(input.lowercase(), chain)
            setNameRecord(nameRecord, input)
        }
    }

    private fun setNameRecord(nameRecord: NameRecord?, input: String) {
        resolveListener?.invoke(nameRecord)
        val isResolve = !nameRecord?.address.isNullOrEmpty() && !nameRecord?.name.isNullOrEmpty()
        state.update {
            State(
                nameRecord = nameRecord,
                isLoading = false,
                isResolve = isResolve,
                isFail = !isResolve && input.isNotEmpty()
            )
        }
    }

    fun onResolved(onResolved: (NameRecord?) -> Unit) {
        this.resolveListener = onResolved
    }

    data class State(
        val isLoading: Boolean = false,
        val isResolve: Boolean = false,
        val isFail: Boolean = false,
        val nameRecord: NameRecord? = null,
    )
}