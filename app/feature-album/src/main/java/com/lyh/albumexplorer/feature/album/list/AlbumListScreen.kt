package com.lyh.albumexplorer.feature.album.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.lyh.albumexplorer.feature.album.model.AlbumUi
import com.lyh.albumexplorer.feature.core.*
import com.lyh.albumexplorer.feature.core.R
import com.lyh.albumexplorer.feature.core.ui.AppTopBar
import com.lyh.albumexplorer.feature.core.ui.ErrorComponent
import com.lyh.albumexplorer.feature.core.ui.LoadingComponent
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AlbumListRoute(
    modifier: Modifier = Modifier,
    onNavigateToAlbum: (albumId: String) -> Unit,
    viewModel: AlbumListViewModel = getViewModel()
) {
    val state by viewModel.albums.collectAsStateWithLifecycle()
    AlbumListScreen(
        modifier = modifier,
        state = state,
        retry = viewModel::triggerAlbums,
        onNavigateToAlbum = onNavigateToAlbum
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumListScreen(
    modifier: Modifier = Modifier,
    state: Resource<List<AlbumUi>>,
    retry: () -> Unit,
    onNavigateToAlbum: (albumId: String) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = { AppTopBar(title = stringResource(id = R.string.app_name), onBackClick = null) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when (state) {
                is ResourceError -> ErrorComponent(
                    errorText = state.errorMessage.getMessage(
                        LocalContext.current
                    ),
                    retry = retry
                )
                is ResourceLoading -> LoadingComponent(loadingText = stringResource(id = R.string.loading_data))
                is ResourceSuccess ->
                    LazyColumn(
                        modifier = modifier
                            .fillMaxSize()
                    ) {
                        state.data.forEach {
                            item {
                                AlbumItemCard(albumUi = it, onClick = onNavigateToAlbum)
                            }
                        }
                    }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumItemCard(
    modifier: Modifier = Modifier,
    albumUi: AlbumUi,
    onClick: (albumId: String) -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = { onClick(albumUi.id.toString()) },
        modifier = modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = albumUi.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(130.dp)
                    .padding(10.dp)
            )
            Text(
                albumUi.title,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
        }
    }

}