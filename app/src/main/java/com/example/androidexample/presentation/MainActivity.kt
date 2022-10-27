package com.example.androidexample.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.rxjava3.subscribeAsState
import com.example.androidexample.ui.theme.AndroidExampleTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Once Hilt is set up in your Application class and an application-level
 * component is available, Hilt can provide dependencies to other Android
 * classes that have the @AndroidEntryPoint annotation.
 *
 * @AndroidEntryPoint generates an individual Hilt component for each
 * Android class in your project.
 * */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidExampleTheme {
                viewModel.uiModel.subscribeAsState(initial = null).value?.let { uiModel ->
                    MainScreen(uiModel = uiModel)
                }
            }
        }
    }
}
