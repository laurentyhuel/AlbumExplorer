package com.lyh.albumexplorer.feature.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lyh.albumexplorer.feature.core.R

@Composable
fun ErrorComponent(
    modifier: Modifier = Modifier,
    errorText: String,
    retry: (() -> Unit)?
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {

        Image(painter = painterResource(id = R.drawable.ic_warning),contentDescription = "")
        Text(
            errorText,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(10.dp)
        )
        retry?.let {
            Button(onClick = retry) {
                Text(
                    text = stringResource(id = R.string.retry)
                )
            }
        }
    }
}