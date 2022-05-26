package com.example.androidexample.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidexample.ui.theme.AndroidExampleTheme
import com.example.androidexample.util.LOADING_VIEW_LOADING_INDICATOR

@Composable
fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = AndroidExampleTheme.colors.onBackground,
            modifier = Modifier.testTag(LOADING_VIEW_LOADING_INDICATOR)
        )
    }
}

@Preview
@Composable
private fun LoadingPreview() {
    Loading()
}
