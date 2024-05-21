package com.odesa.musicMatters.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable

@Composable
fun AdaptiveSnackBar( snackBarData: SnackbarData ) = Snackbar(
    snackbarData = snackBarData,
    containerColor = MaterialTheme.colorScheme.surface,
    contentColor = MaterialTheme.colorScheme.onSurface,
    actionColor = MaterialTheme.colorScheme.primary,
    actionContentColor = MaterialTheme.colorScheme.primary,
    dismissActionContentColor = MaterialTheme.colorScheme.onSurface
)
