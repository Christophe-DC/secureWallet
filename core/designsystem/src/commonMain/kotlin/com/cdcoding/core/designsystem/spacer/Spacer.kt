package com.cdcoding.core.designsystem.spacer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Spacer2() {
    Spacer(modifier = Modifier.size(2.dp))
}

@Composable
fun Spacer4() {
    Spacer(modifier = Modifier.size(4.dp))
}

@Composable
fun Spacer6() {
    Spacer(modifier = Modifier.size(6.dp))
}

@Composable
fun Spacer8() {
    Spacer(modifier = Modifier.size(8.dp))
}

@Composable
fun Spacer16() {
    Spacer(modifier = Modifier.size(16.dp))
}