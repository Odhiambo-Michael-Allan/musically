package com.odesa.musically.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.odesa.musically.ui.settings.SettingDialogOption
import com.odesa.musically.ui.settings.components.SettingsTileDefaults
import com.odesa.musically.ui.theme.MusicallyTheme

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
                            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 12.dp)
                            .weight(1f)
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

@Preview( showBackground = true )
@Composable
fun ScaffoldDialogPreview() {
    MusicallyTheme {
        ScaffoldDialog(
            title = { Text( text = "Language" ) },
            content = {
                val values = listOf(
                    SettingDialogOption( "English", "English" ),
                    SettingDialogOption( "Belarusian", "Belarusian" ),
                    SettingDialogOption( "Chinese", "Chinese" ),
                    SettingDialogOption( "French", "French" ),
                    SettingDialogOption( "Deutsch", "Deutsch" ),
                    SettingDialogOption( "Italiano", "Italiano" ),
                    SettingDialogOption( "Portuguese", "Portuguese" ),
                    SettingDialogOption( "Romanian", "Romanian" ),
                    SettingDialogOption( "Russian", "Russian" ),
                    SettingDialogOption( "Spanish", "Spanish" ),
                    SettingDialogOption( "Turkish", "Turkish" ),
                    SettingDialogOption( "Ukrainian", "Ukrainian" )
                )

                LazyColumn {
                    items( values ) {
                        DialogOption(
                            selected = false,
                            title = it.value,
                            caption = it.caption
                        ) {}
                    }
                }
            },
            onDismissRequest = {}
        )
    }
}

@OptIn( ExperimentalMaterial3Api::class )
@Composable
fun DialogOption(
    selected: Boolean,
    title: String,
    caption: String? = null,
    onClick: () -> Unit,
) {
    Card(
        colors = SettingsTileDefaults.cardColors(),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding( 12.dp ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick
            )
            Spacer( modifier = Modifier.width( 8.dp ) )
            Column {
                Text( text = title )
                caption?.let {
                    Spacer( modifier = Modifier.height( 2.dp ) )
                    Text(
                        caption,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = LocalContentColor.current.copy( alpha = 0.7f )
                        )
                    )
                }
            }
        }
    }
}

@Preview( showBackground = true )
@Composable
fun DialogOptionPreview() {
    MusicallyTheme {
        DialogOption(
            selected = true,
            title = "English",
            caption = "English",
            onClick = {}
        )
    }
}
