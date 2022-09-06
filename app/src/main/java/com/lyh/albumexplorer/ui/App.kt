package com.lyh.albumexplorer.ui

import androidx.compose.runtime.Composable
import com.lyh.albumexplorer.NavHost

@Composable
fun App(
    appState: AppState = rememberAppState()
) {
    AppTheme {
        NavHost(
            navController = appState.navController,
            onBackClick = appState::onBackClick,
            onNavigateToDestination = appState::navigate,
        )
    }

}