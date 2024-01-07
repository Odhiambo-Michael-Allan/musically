package com.odesa.musically.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.odesa.musically.utils.runFunctionIfTrueElseReturnThisObject

@Composable
fun TopAppBarMinimalTitle(
    modifier: Modifier = Modifier,
    fillMaxWidth: Boolean = true,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .runFunctionIfTrueElseReturnThisObject( fillMaxWidth ) { fillMaxWidth() }
            .padding( 8.dp ),
        contentAlignment = Alignment.Center
    ) {
        ProvideTextStyle(
            value = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp,
                textAlign = TextAlign.Center
            )
        ) {
            content()
        }
    }
}

@Preview( showBackground = true )
@Composable
fun TopAppBarMinimalTitlePreview() {
    TopAppBarMinimalTitle {
        Text( text = "Settings" )
    }
}