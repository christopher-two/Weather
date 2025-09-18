package org.christophertwo.weather.presentation.features.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

@Composable
fun ClockRow(
    timeState: MainState.TimeState,
    dateState: MainState.DateState,
    secondsState: MainState.SecondsState,
    isLoading: Boolean = false,
    error: String? = null,
    modifier: Modifier = Modifier,
    compact: Boolean = false // when true use smaller sizes for 16:9 layout
) {
    if (error != null) {
        Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(text = error, color = colorScheme.error)
        }
        return
    }

    // Si todos los subestados están vacíos mostramos un mensaje de vacío
    if (timeState.isEmpty && dateState.isEmpty && secondsState.isEmpty) {
        Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(text = "No hay datos de hora disponibles", color = colorScheme.onBackground.copy(alpha = 0.6f))
        }
        return
    }

    // use centralized tokens
    val t = SizeTokens.of(compact)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isLoading) {
            // placeholder para hora/minutos
            Column(horizontalAlignment = Alignment.Start) {
                Box(
                    modifier = Modifier
                        .height(t.bigBoxH)
                        .width(t.bigBoxW)
                        .background(colorScheme.onBackground.copy(alpha = 0.12f))
                )
                Spacer(modifier = Modifier.height(t.spacerSmall))
                Box(
                    modifier = Modifier
                        .height(t.bigBoxH)
                        .width(t.bigBoxW)
                        .background(colorScheme.onBackground.copy(alpha = 0.12f))
                )
            }
            Spacer(modifier = Modifier.width(t.spacerMedium))
            Column(horizontalAlignment = Alignment.Start) {
                Box(
                    modifier = Modifier
                        .height(t.smallH)
                        .width(t.smallW1)
                        .background(colorScheme.onBackground.copy(alpha = 0.12f))
                )
                Spacer(modifier = Modifier.height(t.spacerSmall))
                Box(
                    modifier = Modifier
                        .height(t.smallH)
                        .width(t.smallW2)
                        .background(colorScheme.onBackground.copy(alpha = 0.12f))
                )
                Spacer(modifier = Modifier.height(t.spacerSmall))
                // segundos placeholder
                Column(modifier = Modifier.offset(x = t.offsetX, y = t.offsetY)) {
                    Box(
                        modifier = Modifier
                            .height(if (compact) t.smallH * 0.75f else t.smallH)
                            .width(if (compact) (t.smallW1 * 0.4f) else (t.smallW1 * 0.4f))
                            .background(colorScheme.onBackground.copy(alpha = 0.08f))
                    )
                    Spacer(modifier = Modifier.height(4.sdp))
                    Box(
                        modifier = Modifier
                            .height(if (compact) t.smallH * 0.75f else t.smallH)
                            .width(if (compact) (t.smallW1 * 0.4f) else (t.smallW1 * 0.4f))
                            .background(colorScheme.onBackground.copy(alpha = 0.12f))
                    )
                }
            }
        } else {
            // Mostrar valores o placeholders individuales si están vacíos
            if (timeState.isEmpty) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "--",
                        fontSize = t.timeFont,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onBackground.copy(alpha = 0.4f)
                    )
                    Text(
                        text = "--",
                        fontSize = t.timeFont,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onBackground.copy(alpha = 0.4f)
                    )
                }
            } else {
                TimeColumn(hours = timeState.hours, minutes = timeState.minutes, compact = compact)
            }

            Spacer(modifier = Modifier.width(t.spacerMedium))

            if (dateState.isEmpty && secondsState.isEmpty) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "--",
                        fontSize = t.dateFont,
                        fontWeight = FontWeight.Medium,
                        color = colorScheme.onBackground.copy(alpha = 0.4f)
                    )
                }
            } else {
                DateColumn(
                    day = if (dateState.isEmpty) "--" else dateState.day,
                    date = if (dateState.isEmpty) "--" else dateState.date,
                    secondsPrev = if (secondsState.isEmpty) "--" else secondsState.prev,
                    secondsCurr = if (secondsState.isEmpty) "--" else secondsState.current,
                    secondsNext = if (secondsState.isEmpty) "--" else secondsState.next,
                    compact = compact
                )
            }
        }
    }
}

@Composable
fun TimeColumn(hours: String, minutes: String, modifier: Modifier = Modifier, compact: Boolean = false) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        val t = SizeTokens.of(compact)
        Text(
            text = hours,
            fontSize = t.timeFont,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground,
            lineHeight = t.timeLine
        )
        Text(
            text = minutes,
            fontSize = t.timeFont,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground,
            lineHeight = t.timeLine
        )
    }
}

@Composable
fun DateColumn(
    day: String,
    date: String,
    secondsPrev: String,
    secondsCurr: String,
    secondsNext: String,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        val t = SizeTokens.of(compact)
        Text(
            text = day,
            fontSize = t.dateFont,
            fontWeight = FontWeight.Medium,
            color = colorScheme.onBackground
        )
        Text(
            text = date,
            fontSize = t.dateFont,
            fontWeight = FontWeight.Medium,
            color = colorScheme.onBackground
        )
        SecondsStack(prev = secondsPrev, current = secondsCurr, next = secondsNext, compact = compact)
    }
}

@Composable
fun SecondsStack(prev: String, current: String, next: String, modifier: Modifier = Modifier, compact: Boolean = false) {
    val t = SizeTokens.of(compact)
    Column(
        modifier = modifier.offset(x = t.offsetX, y = t.offsetY),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = prev,
            fontSize = t.secFont,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground.copy(alpha = 0.18f),
            modifier = Modifier.padding(bottom = if (compact) 1.sdp else 2.sdp)
        )
        Text(
            text = current,
            fontSize = t.secFont,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground
        )
        Text(
            text = next,
            fontSize = t.secFont,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground.copy(alpha = 0.12f),
            modifier = Modifier
                .padding(top = if (compact) 3.sdp else 4.sdp)
                .graphicsLayer {
                    scaleY = -1f
                    translationY = 8f
                    alpha = 0.5f
                }
        )
    }
}

@Composable
fun CityDisplay(
    cityState: MainState.CityState,
    isLoading: Boolean = false,
    error: String? = null,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    if (error != null) {
        Text(text = error, color = colorScheme.error, modifier = modifier)
        return
    }

    if (isLoading) {
        // placeholder
        Box(modifier = modifier) {
            Column {
                val phH = if (compact) 40.sdp else 48.sdp
                val phGap = if (compact) 6.sdp else 8.sdp
                Box(
                    modifier = Modifier
                        .height(phH)
                        .fillMaxWidth(0.6f)
                        .background(colorScheme.onBackground.copy(alpha = 0.12f))
                )
                Spacer(modifier = Modifier.height(phGap))
                Box(
                    modifier = Modifier
                        .height(phH)
                        .fillMaxWidth(0.5f)
                        .background(colorScheme.onBackground.copy(alpha = 0.12f))
                )
            }
        }
        return
    }

    if (cityState.isEmpty) {
        Text(text = "Ciudad no disponible", color = colorScheme.onBackground.copy(alpha = 0.6f), modifier = modifier)
        return
    }

    val cityFont = if (compact) 32.ssp else 40.ssp
    val cityLine = if (compact) 36.ssp else 44.ssp
    Text(
        modifier = modifier,
        text = cityState.text,
        fontSize = cityFont,
        fontWeight = FontWeight.Bold,
        color = colorScheme.onBackground,
        lineHeight = cityLine
    )
}

@Composable
fun SunInfo(
    sunState: MainState.SunState,
    isLoading: Boolean = false,
    error: String? = null,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    if (error != null) {
        Text(text = error, color = colorScheme.error, modifier = modifier)
        return
    }

    if (isLoading) {
        val sh = if (compact) 14.sdp else 18.sdp
        val gap = if (compact) 4.sdp else 6.sdp
        Column(modifier = modifier) {
            Box(
                modifier = Modifier
                    .height(sh)
                    .fillMaxWidth(0.4f)
                    .background(colorScheme.onBackground.copy(alpha = 0.12f))
            )
            Spacer(modifier = Modifier.height(gap))
            Box(
                modifier = Modifier
                    .height(sh)
                    .fillMaxWidth(0.3f)
                    .background(colorScheme.onBackground.copy(alpha = 0.12f))
            )
        }
        return
    }

    if (sunState.isEmpty) {
        Text(
            text = "Información solar no disponible",
            color = colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = modifier
        )
        return
    }

    val sunFont = if (compact) 16.ssp else 18.ssp
    Column(modifier = modifier) {
        Text(
            text = sunState.info,
            fontSize = sunFont,
            fontWeight = FontWeight.Medium,
            color = colorScheme.onBackground
        )
        Text(
            text = sunState.range,
            fontSize = sunFont,
            fontWeight = FontWeight.Medium,
            color = colorScheme.onBackground
        )
    }
}

@Composable
fun WeatherInfo(
    weatherState: MainState.WeatherState,
    isLoading: Boolean = false,
    error: String? = null,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    if (error != null) {
        Text(text = error, color = colorScheme.error, modifier = modifier)
        return
    }

    val loading = isLoading || weatherState.isLoading
    val err = error ?: weatherState.error
    val t = SizeTokens.of(compact)

    if (err != null) {
        Text(text = err, color = colorScheme.error, modifier = modifier)
        return
    }

    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        if (loading) {
            // placeholders similares al resto de componentes
            Box(
                modifier = Modifier
                    .height(if (compact) t.smallH else t.bigBoxH)
                    .fillMaxWidth(0.6f)
                    .background(colorScheme.onBackground.copy(alpha = 0.12f))
            )
            Spacer(modifier = Modifier.height(t.spacerSmall))
            Box(
                modifier = Modifier
                    .height(if (compact) t.smallH else t.smallH)
                    .fillMaxWidth(0.4f)
                    .background(colorScheme.onBackground.copy(alpha = 0.12f))
            )
            return
        }

        if (weatherState.isEmpty) {
            Text(
                text = "No hay datos del clima disponibles",
                color = colorScheme.onBackground.copy(alpha = 0.6f)
            )
            return
        }

        // descripción
        weatherState.description?.let {
            Text(
                text = it,
                fontSize = t.dateFont,
                fontWeight = FontWeight.Medium,
                color = colorScheme.onBackground.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(t.spacerSmall))
        }

        // temperatura destacada (usa tamaño similar a la hora)
        Text(
            text = weatherState.temperature,
            fontSize = t.timeFont,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(t.spacerSmall))

        // detalles secundarios
        Column {
            weatherState.humidity?.let {
                Text(
                    text = "Humedad: $it",
                    fontSize = t.dateFont,
                    color = colorScheme.onBackground.copy(alpha = 0.85f)
                )
            }
            weatherState.pressure?.let {
                Text(
                    text = "Presión: $it",
                    fontSize = t.dateFont,
                    color = colorScheme.onBackground.copy(alpha = 0.85f)
                )
            }
            weatherState.windSpeed?.let {
                Text(
                    text = "Viento: $it",
                    fontSize = t.dateFont,
                    color = colorScheme.onBackground.copy(alpha = 0.85f)
                )
            }
        }
    }
}
