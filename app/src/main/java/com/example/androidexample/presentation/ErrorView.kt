package com.example.androidexample.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidexample.R
import com.example.androidexample.ui.theme.AndroidExampleTheme

const val ERROR_VIEW_RETRY_BUTTON = "retryButton"

@Composable
fun Error(onClickRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClickRetry,
            modifier = Modifier.testTag(ERROR_VIEW_RETRY_BUTTON)
        ) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}

@Preview
@Composable
private fun ErrorPreview() {
    AndroidExampleTheme {
        Error(onClickRetry = {})
    }
}
