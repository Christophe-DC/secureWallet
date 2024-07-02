package com.cdcoding.system.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Dimens(val margin: Dp)

val smallMarginDimens = Dimens(margin = 6.dp)

val mediumMarginDimens = Dimens(margin = 8.dp)

val largeMarginDimens = Dimens(margin = 12.dp)

val LocalDimens = staticCompositionLocalOf { smallMarginDimens }