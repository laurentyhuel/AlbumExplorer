package com.lyh.albumexplorer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.lyh.albumexplorer.feature.album.nav.AlbumDestination
import com.lyh.albumexplorer.feature.album.nav.AlbumListNavigation
import com.lyh.albumexplorer.feature.album.nav.albumGraph
import com.lyh.albumexplorer.feature.core.NavigationDestination

@Composable
fun NavHost(
    navController: NavHostController,
    onNavigateToDestination: (NavigationDestination, String) -> Unit,
    startDestination: String = AlbumListNavigation.route,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        albumGraph(
            navigateToAlbum = {
                onNavigateToDestination(
                    AlbumDestination, AlbumDestination.createNavigationRoute(it)
                )
            },
            onBackClick = onBackClick
        )
    }
}