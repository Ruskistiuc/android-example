package com.example.androidexample.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.androidexample.R
import com.example.androidexample.ui.theme.AndroidExampleTheme

const val EMPTY_VIEW_TEXT = "emptyViewText"

@Composable
fun Empty() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = {
            Text(
                text = stringResource(id = R.string.empty_view_title),
                style = AndroidExampleTheme.typography.h4,
                modifier = Modifier.testTag(EMPTY_VIEW_TEXT),
            )
        }
    )
}

@Composable
@PreviewLightDark
private fun EmptyPreview() = AndroidExampleTheme { Surface { Empty() } }
