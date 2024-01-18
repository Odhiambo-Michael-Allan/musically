package com.odesa.musically.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

object ScaffoldDialogDefaults {
    const val PreferredMaxHeight = 0.8f
}

@Composable
fun ScaffoldDialog(
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
    actions: ( @Composable RowScope.() -> Unit )? = null,
    onDismissRequest: () -> Unit,
) {

    val configuration = LocalConfiguration.current

    Dialog( onDismissRequest = onDismissRequest ) {
        Surface(
            shape = RoundedCornerShape( 8.dp ),
            modifier = Modifier.run {
                val maxHeight = ( configuration.screenHeightDp * 0.9f ).dp
                requiredHeightIn( max = maxHeight )
            }
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding( start = 20.dp, end = 20.dp, top = 12.dp, bottom = 12.dp )
                            .weight( 1f )
                    ) {
                        ProvideTextStyle(
                            value = MaterialTheme.typography.bodyLarge.copy(
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            title()
                        }
                    }
                }
                Divider()
                Box {
                    content()
                }
                actions?.let {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding( 12.dp, 0.dp )
                    ) {
                        actions()
                    }
                }
            }
        }
    }
}

//@Preview( showBackground = true )
//@Composable
//fun ScaffoldDialogPreview() {
//    MusicallyTheme {
//        ScaffoldDialog(
//            title = { Text( text = "Language" ) },
//            content = {
//                val values = listOf(
//                    Language( "English", "English", EnglishTranslation.locale ),
//                    Language( "Беларуская", "Belarusian", BelarusianTranslation.locale ),
//                    Language( "简体中文", "Chinese", ChineseTranslation.locale ),
//                    Language( "Français", "French", FrenchTranslation.locale ),
//                    Language( "Deutsch", "Deutsch", GermanTranslation.locale ),
//                    Language( "Español", "Spanish", SpanishTranslation.locale ),
//                )
//
//                LazyColumn {
//                    items( values ) {
//                        DialogOption(
//                            selected = false,
//                            title = it.nativeName,
//                            caption = it.englishName,
//                            onClick = {}
//                        )
//                    }
//                }
//            },
//            onDismissRequest = {}
//        )
//    }
//}


