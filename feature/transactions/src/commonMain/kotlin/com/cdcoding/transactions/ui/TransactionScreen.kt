package com.cdcoding.transactions.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.activity_empty_state_message
import com.cdcoding.core.resource.activity_title
import com.cdcoding.transactions.presentation.TransactionsIntent
import com.cdcoding.transactions.presentation.TransactionsUIState
import com.cdcoding.transactions.presentation.TransactionsViewModel
import com.cdcoding.transactions.ui.component.TransactionsList
import org.jetbrains.compose.resources.stringResource


class TransactionsScreen : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Filled.History)
            val title = stringResource(Res.string.activity_title)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel: TransactionsViewModel = useInject()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        TransactionsScreenContent(
            uiState = uiState.value,
            onIntent = viewModel::setIntent
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TransactionsScreenContent(
    uiState: TransactionsUIState,
    onIntent: (TransactionsIntent) -> Unit,
    listState: LazyListState = rememberLazyListState(),
) {

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.loading,
        onRefresh = { onIntent(TransactionsIntent.OnRefresh) }
    )
        Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
            when {
                uiState.transactions.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .wrapContentHeight(align = Alignment.CenterVertically),
                            text = stringResource(Res.string.activity_empty_state_message),
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                    ) {
                        TransactionsList(
                            items = uiState.transactions
                        )
                    }
                }
            }
            PullRefreshIndicator(
                uiState.loading,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter)
            )
        }
}
