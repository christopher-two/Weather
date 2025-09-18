package org.christophertwo.weather.presentation.features.main

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import network.chaintech.sdpcomposemultiplatform.sdp
import org.christophertwo.weather.presentation.theme.WeatherTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("UNUSED_PARAMETER")
@Composable
fun MainScreen16By9(
    state: MainState,
    onAction: (MainAction) -> Unit = {}
) {
    val bgColor =
        state.backgroundColor?.let { Color(it.toULong()) } ?: if (isSystemInDarkTheme()) Color(0xFF121212) else Color(
            0xFFFFFFFF
        )

    Scaffold(containerColor = bgColor) { padding ->
        // No constraints: usar Row con weights para redistribuir elementos en 16:9
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
                .padding(padding)
                .padding(horizontal = 24.sdp, vertical = 40.sdp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Columna principal (izquierda) — ocupa 55% del ancho
            Column(
                modifier = Modifier
                    .weight(0.55f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                // Reloj grande — parte superior
                Box(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxWidth()
                ) {
                    // ClockRow ocupa su caja sin escalar
                    ClockRow(
                        timeState = state.time,
                        dateState = state.date,
                        secondsState = state.seconds,
                        isLoading = state.time.isLoading || state.date.isLoading || state.seconds.isLoading,
                        error = state.time.error ?: state.date.error ?: state.seconds.error,
                        compact = true
                    )
                }

                // Ciudad — centro
                Box(
                    modifier = Modifier
                        .weight(0.3f)
                        .fillMaxWidth()
                ) {
                    // CityDisplay en modo compacto para 16:9
                    CityDisplay(
                        cityState = state.city,
                        isLoading = state.city.isLoading,
                        error = state.city.error,
                        modifier = Modifier,
                        compact = true
                    )
                }
            }

            // Columna secundaria (derecha) — información complementaria
            Column(
                modifier = Modifier
                    .weight(0.45f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.End
            ) {
                // Sun info en la esquina superior derecha
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    // WeatherInfo en modo compacto para 16:9 (reemplaza SunInfo)
                    WeatherInfo(
                        weatherState = state.weather,
                        isLoading = state.weather.isLoading,
                        error = state.weather.error,
                        modifier = Modifier,
                        compact = true
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 720)
@Composable
private fun Preview16By9() {
    WeatherTheme {
        MainScreen16By9(state = MainState(), onAction = {})
    }
}
