package com.odesa.musically.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AdaptiveSnackBar( snackBarData: SnackbarData ) = Snackbar(
    snackbarData = snackBarData,
    containerColor = MaterialTheme.colorScheme.surface,
    contentColor = MaterialTheme.colorScheme.onSurface,
    actionColor = MaterialTheme.colorScheme.primary,
    actionContentColor = MaterialTheme.colorScheme.primary,
    dismissActionContentColor = MaterialTheme.colorScheme.onSurface
)
