package com.example.androidexample.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.rxjava3.subscribeAsState
import com.example.androidexample.ui.theme.AndroidExampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidExampleTheme {
                viewModel.uiModel.subscribeAsState(initial = null).value?.let { screenState ->
                    MainScreen(screenState = screenState)
                }
            }
        }
    }
}
