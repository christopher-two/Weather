package org.christophertwo.weather.presentation.features.main

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import org.christophertwo.weather.presentation.theme.WeatherTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ClockRowPreviews() {
    WeatherTheme {
        Column {
            // Normal
            ClockRow(
                timeState = MainState.TimeState(hours = "08", minutes = "40"),
                dateState = MainState.DateState(day = "Thu,", date = "20 Mar"),
                secondsState = MainState.SecondsState(prev = "14", current = "15", next = "16")
            )

            // Loading
            ClockRow(
                timeState = MainState.TimeState(isLoading = true),
                dateState = MainState.DateState(isLoading = true),
                secondsState = MainState.SecondsState(isLoading = true),
                isLoading = true
            )

            // Empty
            ClockRow(
                timeState = MainState.TimeState(isEmpty = true),
                dateState = MainState.DateState(isEmpty = true),
                secondsState = MainState.SecondsState(isEmpty = true)
            )

            // Error
            ClockRow(
                timeState = MainState.TimeState(),
                dateState = MainState.DateState(),
                secondsState = MainState.SecondsState(),
                error = "Error cargando hora"
            )
        }
    }
}

@Preview
@Composable
fun CityDisplayPreviews() {
    WeatherTheme {
        Column {
            CityDisplay(cityState = MainState.CityState(text = "Los\nAngeles"))
            CityDisplay(cityState = MainState.CityState(isLoading = true), isLoading = true)
            CityDisplay(cityState = MainState.CityState(isEmpty = true), isLoading = false)
            CityDisplay(cityState = MainState.CityState(), error = "Error cargando ciudad")
        }
    }
}

@Preview
@Composable
fun SunInfoPreviews() {
    WeatherTheme {
        Column {
            SunInfo(sunState = MainState.SunState(info = "Sun \uD83C\uDF1E · 10h 06m", range = "07:12 - 17:17"))
            SunInfo(sunState = MainState.SunState(isLoading = true), isLoading = true)
            SunInfo(sunState = MainState.SunState(isEmpty = true))
            SunInfo(sunState = MainState.SunState(), error = "Error cargando info solar")
        }
    }
}

