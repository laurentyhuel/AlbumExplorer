package com.lyh.albumexplorer.feature.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lyh.albumexplorer.feature.core.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: (() -> Unit)?
) {
    SmallTopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        title = { Text(text = title) },
        navigationIcon = { if (onBackClick != null) AppTopBarBackButton(onBackClick) }
    )
}

@Composable
private fun AppTopBarBackButton(
    onBackClick: () -> Unit
) {
    IconButton(onClick = onBackClick) {
        Icon(

            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.back),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}