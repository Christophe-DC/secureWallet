package com.cdcoding.selectasset.presentation

import androidx.compose.runtime.Immutable
import com.cdcoding.model.AssetUIState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class SelectAssetState(
    val isLoading: Boolean = false,
    val isAddAssetAvailable: Boolean = false,
    val error: String = "",
    val assets: ImmutableList<AssetUIState> = listOf<AssetUIState>().toImmutableList(),
)