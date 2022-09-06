package com.lyh.albumexplorer.feature.album.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.lyh.albumexplorer.feature.album.model.AlbumUi
import com.lyh.albumexplorer.feature.core.*
import com.lyh.albumexplorer.feature.core.ui.AppTopBar
import com.lyh.albumexplorer.feature.core.ui.ErrorComponent
import com.lyh.albumexplorer.feature.core.ui.LoadingComponent
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AlbumRoute(
    modifier: Modifier = Modifier,
    id: String,
    viewModel: AlbumViewModel = getViewModel(),
    onBackClick: () -> Unit,
) {
    viewModel.setAlbumId(id.toLong())
    val state by viewModel.album.collectAsStateWithLifecycle()
    AlbumScreen(
        modifier = modifier,
        state = state,
        onBackClick = onBackClick
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    modifier: Modifier = Modifier,
    state: Resource<AlbumUi>,
    onBackClick: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = stringResource(id = R.string.app_name),
                onBackClick = onBackClick
            )
        }
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
                    retry = null,
                )
                is ResourceLoading -> LoadingComponent(loadingText = stringResource(id = R.string.loading_data))
                is ResourceSuccess -> AlbumDetail(album = state.data)
            }
        }
    }
}

@Composable
private fun AlbumDetail(
    modifier: Modifier = Modifier,
    album: AlbumUi
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Create references for the composables to constraint
        val (button, title, number) = createRefs()

        AsyncImage(
            model = album.url,
            contentDescription = "",
            modifier = Modifier
                .padding(10.dp)
                .size(200.dp)
                .constrainAs(button) {
                    top.linkTo(parent.top)
                    centerHorizontallyTo(parent)
                }
        )
        Text(
            text = album.title,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .padding(10.dp)
                .constrainAs(title) {
                    top.linkTo(button.bottom)
                    centerHorizontallyTo(parent)
                }
        )

        Text(
            text = stringResource(
                id = com.lyh.albumexplorer.feature.album.R.string.album_number,
                album.id
            ),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .padding(10.dp)
                .constrainAs(number) {
                    bottom.linkTo(button.bottom)
                    end.linkTo(button.start)
                }
        )
    }
}
