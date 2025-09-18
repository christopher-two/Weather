package org.christophertwo.weather.presentation.features.main

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import network.chaintech.sdpcomposemultiplatform.sdp
import org.christophertwo.weather.presentation.theme.WeatherTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainRoot(
    viewModel: MainViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BoxWithConstraints {
        val aspect = if (maxHeight > 0.sdp) maxWidth / maxHeight else 1f
        val isWide = aspect >= 1.6f

        if (isWide) {
            MainScreen16By9(
                state = state,
                onAction = viewModel::onAction
            )
        } else {
            MainScreen(
                state = state,
                onAction = viewModel::onAction
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("UNUSED_PARAMETER")
@Composable
private fun MainScreen(
    state: MainState,
    onAction: (MainAction) -> Unit,
) {
    val bgColor =
        state.backgroundColor?.let { Color(it.toULong()) } ?: if (isSystemInDarkTheme()) Color(0xFF121212) else Color(
            0xFFFFFFFF
        )

    Scaffold(
        containerColor = bgColor,
        content = { padding ->
            // Layout fijo: una sola columna que ocupa toda la pantalla
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bgColor)
                    .padding(padding)
                    .padding(horizontal = 24.sdp, vertical = 32.sdp),
            ) {
                item {
                    ClockRow(
                        timeState = state.time,
                        dateState = state.date,
                        secondsState = state.seconds,
                        isLoading = state.time.isLoading || state.date.isLoading || state.seconds.isLoading,
                        error = state.time.error ?: state.date.error ?: state.seconds.error
                    )
                }

                item { Spacer(modifier = Modifier.height(32.sdp)) }

                item {
                    CityDisplay(
                        cityState = state.city,
                        isLoading = state.city.isLoading,
                        error = state.city.error
                    )
                }

                item { Spacer(modifier = Modifier.height(24.sdp)) }

                item {
                    WeatherInfo(
                        weatherState = state.weather,
                        isLoading = state.weather.isLoading,
                        error = state.weather.error
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    WeatherTheme {
        MainScreen(
            state = MainState(),
            onAction = {}
        )
    }
}