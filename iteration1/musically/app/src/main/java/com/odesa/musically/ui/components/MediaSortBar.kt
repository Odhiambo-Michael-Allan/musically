package com.odesa.musically.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.odesa.musically.data.storage.preferences.SortSongsBy
import com.odesa.musically.data.storage.preferences.impl.SettingsDefaults
import com.odesa.musically.services.i18n.English
import com.odesa.musically.ui.theme.MusicallyTheme

@Composable
fun <T : Enum<T>> MediaSortBar(
    sortReverse: Boolean,
    onSortReverseChange: (Boolean ) -> Unit,
    sortType: T,
    sortTypes: Map<T, String>,
    onSortTypeChange: (T ) -> Unit,
    label: @Composable () -> Unit,
    onShufflePlay: ( () -> Unit )? = null
) {
    var showDropdownMenu by remember { mutableStateOf( false ) }
//    val currentTextStyle = MaterialTheme.typography.bodySmall.run {
//        copy( color = MaterialTheme.colorScheme.surface )
//    }
//    val iconButtonStyle = IconButtonDefaults.iconButtonColors(
//        contentColor = currentTextStyle.color
//    )
//    val iconModifier = Modifier.size( 20.dp )
//    val textButtonStyle = ButtonDefaults.textButtonColors(
//        contentColor = currentTextStyle.color
//    )

    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            Spacer( modifier = Modifier.width( 8.dp ) )
            IconButton(
                onClick = { onSortReverseChange( !sortReverse ) }
            ) {
                Icon(
                    modifier = Modifier.size( 20.dp ),
                    imageVector = if ( sortReverse ) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                    contentDescription = null
                )
            }
            Box {
                TextButton(
                    onClick = { showDropdownMenu = !showDropdownMenu }
                ) {
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        text = sortTypes[ sortType ]!!,
                    )
                }
                DropdownMenu(
                    expanded = showDropdownMenu,
                    onDismissRequest = { showDropdownMenu = false }
                ) {
                    sortTypes.map {
                        val onClick = {
                            showDropdownMenu = false
                            onSortTypeChange( it.key )
                        }
                        DropdownMenuItem(
                            contentPadding = MenuDefaults.DropdownMenuItemContentPadding.run {
                                val horizontalPadding = calculateLeftPadding( LayoutDirection.Ltr )
                                PaddingValues(
                                    start = horizontalPadding.div( 2 ),
                                    end = horizontalPadding.times( 4 )
                                )
                            },
                            leadingIcon = {
                                RadioButton(
                                    selected = it.key == sortType,
                                    onClick = onClick
                                )
                            },
                            text = {
                                Text( text = it.value )
                            },
                            onClick = onClick,
                        )
                    }
                }
            }
        }
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProvideTextStyle( value = MaterialTheme.typography.bodySmall ) {
                label()
            }
            onShufflePlay?.let {
                IconButton(
                    modifier = Modifier.padding( 4.dp, 0.dp ),
                    onClick = it
                ) {
                    Icon(
                        modifier = Modifier.size( 20.dp ),
                        imageVector = Icons.Filled.Shuffle,
                        contentDescription = null
                    )
                }
            } ?: Spacer( modifier = Modifier.width( 20.dp ) )
        }
    }
}

@Preview( showSystemUi = true )
@Composable
fun MediaSortBarPreview() {
    MusicallyTheme(
        themeMode = SettingsDefaults.themeMode,
        primaryColorName = SettingsDefaults.primaryColorName,
        fontName = SettingsDefaults.font.name,
        fontScale = SettingsDefaults.fontScale,
        useMaterialYou = SettingsDefaults.useMaterialYou
    ) {
        MediaSortBar(
            sortReverse = false,
            onSortReverseChange = {},
            sortType = SortSongsBy.TITLE,
            sortTypes = SortSongsBy.entries.associateBy( { it }, { it.label( English ) } ),
            onSortTypeChange = {},
            label = { Text(text = English.xSongs( "42" ) ) }
        )
    }
}