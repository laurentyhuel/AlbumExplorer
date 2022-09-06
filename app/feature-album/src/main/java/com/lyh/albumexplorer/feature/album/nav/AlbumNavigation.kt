package com.lyh.albumexplorer.feature.album.nav

import android.net.Uri
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lyh.albumexplorer.feature.album.detail.AlbumRoute
import com.lyh.albumexplorer.feature.album.list.AlbumListRoute
import com.lyh.albumexplorer.feature.core.NavigationDestination

object AlbumListNavigation : NavigationDestination {
    override val route = "albums_route"
    override val destination = "albums_destination"
}


object AlbumDestination : NavigationDestination {
    const val albumIdArg = "albumId"
    override val route = "album_route/{$albumIdArg}"
    override val destination = "album_destination"

    /**
     * Creates destination route for an albumId that could include special characters
     */
    fun createNavigationRoute(albumIdArg: String): String {
        val encodedId = Uri.encode(albumIdArg)
        return "album_route/$encodedId"
    }

    /**
     * Returns the albumId from a [NavBackStackEntry] after an album destination navigation call
     */
    fun fromNavArgs(entry: NavBackStackEntry): String {
        val encodedId = entry.arguments?.getString(albumIdArg)!!
        return Uri.decode(encodedId)
    }
}

fun NavGraphBuilder.albumGraph(
    navigateToAlbum: (String) -> Unit,
    onBackClick: () -> Unit,
) {

    composable(route = AlbumListNavigation.route) {
        AlbumListRoute(onNavigateToAlbum = navigateToAlbum)
    }
    composable(
        route = AlbumDestination.route,
        arguments = listOf(
            navArgument(AlbumDestination.albumIdArg) { type = NavType.StringType }
        )
    ) {
        AlbumRoute(
            id = AlbumDestination.fromNavArgs(it),
            onBackClick = onBackClick)
    }    
}
