@file:Suppress("DEPRECATION")
package org.christophertwo.weather.presentation.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.christophertwo.weather.domain.usecase.FetchWeatherUseCase
import kotlin.time.ExperimentalTime

class MainViewModel(
    private val fetchWeatherUseCase: FetchWeatherUseCase
) : ViewModel() {

    private var hasLoadedInitialData = false
    // Offset en segundos de la zona horaria del lugar consultado. Si es null,
    // se usa la zona horaria del sistema.
    private var timezoneOffsetSeconds: Int? = null

    // Nuevo: base de tiempo remoto (epoch UTC segundos) y el epoch del sistema en el momento de la captura.
    // remoteBaseLocalEpochSeconds = (dt UTC) + timezone offset -> epoch seconds representing local wall-clock time
    private var remoteBaseLocalEpochSeconds: Long? = null
    private var remoteBaseSystemEpochMillis: Long? = null

    private val _state = MutableStateFlow(MainState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                // Carga inicial usando la ciudad del estado por defecto
                val defaultCity = _state.value.city.text.substringBefore(",").trim()
                onAction(MainAction.FetchWeather(defaultCity))
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MainState()
        )

    init {
        startClock()
    }

    fun onAction(action: MainAction) {
        when (action) {
            is MainAction.FetchWeather -> {
                fetchWeather(action.city)
            }

            MainAction.Refresh -> {
                val currentCity = _state.value.city.text.substringBefore(",").trim()
                fetchWeather(currentCity)
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun startClock() {
        viewModelScope.launch {
            val days = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
            val months = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")

            // Primer update inmediato
            updateClockValues(days, months)

            while (isActive) {
                // sincronizar con el inicio del siguiente segundo para evitar drift
                val nowMillis = kotlin.time.Clock.System.now().toEpochMilliseconds()
                val delayMillis = 1000L - (nowMillis % 1000L)
                delay(delayMillis)

                updateClockValues(days, months)
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun updateClockValues(days: List<String>, months: List<String>) {
        val nowMillis = kotlin.time.Clock.System.now().toEpochMilliseconds()

        // Si tenemos una base remota capturada (dt UTC + timezone) la usamos para avanzar
        // la hora local remota por el tiempo transcurrido desde la captura.
        val ldt = if (remoteBaseLocalEpochSeconds != null && remoteBaseSystemEpochMillis != null) {
            val elapsedMillis = nowMillis - remoteBaseSystemEpochMillis!!
            val currentRemoteLocalEpochSec = remoteBaseLocalEpochSeconds!! + (elapsedMillis / 1000L)
            println("[MainViewModel] Using remote base local epoch. remoteBaseLocalEpochSeconds=$remoteBaseLocalEpochSeconds remoteBaseSystemEpochMillis=$remoteBaseSystemEpochMillis elapsedMillis=$elapsedMillis currentRemoteLocalEpochSec=$currentRemoteLocalEpochSec")
            // currentRemoteLocalEpochSec already represents local wall-clock epoch seconds, convert with UTC timezone
            Instant.fromEpochSeconds(currentRemoteLocalEpochSec).toLocalDateTime(TimeZone.UTC)
        } else if (timezoneOffsetSeconds != null) {
            // Fallback: aplicar offset directamente sobre el epoch actual
            val nowEpochSec = nowMillis / 1000L
            val adjusted = Instant.fromEpochSeconds(nowEpochSec + timezoneOffsetSeconds!!.toLong())
            println("[MainViewModel] Using timezone offset fallback. offset=${timezoneOffsetSeconds} nowEpochSec=$nowEpochSec adjustedEpochSec=${nowEpochSec + timezoneOffsetSeconds!!.toLong()}")
            adjusted.toLocalDateTime(TimeZone.UTC)
        } else {
            // comportamiento por defecto: hora local del sistema
            val nowInstant = Instant.fromEpochMilliseconds(nowMillis)
            println("[MainViewModel] Using system timezone. nowMillis=$nowMillis")
            nowInstant.toLocalDateTime(TimeZone.currentSystemDefault())
        }

        val hh = ldt.hour.toString().padStart(2, '0')
        val mm = ldt.minute.toString().padStart(2, '0')

        val sec = ldt.second
        val prev = ((sec + 59) % 60).toString().padStart(2, '0')
        val cur = sec.toString().padStart(2, '0')
        val next = ((sec + 1) % 60).toString().padStart(2, '0')

        val dayName = days[ldt.dayOfWeek.ordinal]
        val dateText = "${ldt.day} ${months[ldt.month.number - 1]}"

        // Actualiza solo las partes de tiempo/fecha/segundos
        _state.value = _state.value.copy(
            time = _state.value.time.copy(
                hours = hh,
                minutes = mm,
                isLoading = false,
                isEmpty = false,
                error = null
            ),
            date = _state.value.date.copy(
                day = "$dayName,",
                date = dateText,
                isLoading = false,
                isEmpty = false,
                error = null
            ),
            seconds = _state.value.seconds.copy(
                prev = prev,
                current = cur,
                next = next,
                isLoading = false,
                isEmpty = false,
                error = null
            )
        )
    }

    // Helper para formatear offset en segundos a "+HH:MM" o "-HH:MM"
    private fun formatOffset(offsetSeconds: Int): String {
        val sign = if (offsetSeconds >= 0) "+" else "-"
        val abs = kotlin.math.abs(offsetSeconds)
        val hours = (abs / 3600).toString().padStart(2, '0')
        val minutes = ((abs % 3600) / 60).toString().padStart(2, '0')
        return "$sign$hours:$minutes"
    }

    @OptIn(ExperimentalTime::class)
    private fun fetchWeather(city: String) {
        viewModelScope.launch {
            // set loading flags
            _state.value = _state.value.copy(
                isLoading = true,
                error = null,
                city = _state.value.city.copy(isLoading = true, error = null),
                weather = _state.value.weather.copy(isLoading = true, error = null)
            )

            try {
                val result = fetchWeatherUseCase(city)
                result.fold(onSuccess = { weather ->
                    val cityText = buildString {
                        append(weather.city)
                        weather.country?.let {
                            if (it.isNotBlank()) append(", ").append(it)
                        }
                    }

                    // Map domain weather to UI strings
                    val tempText = weather.temperature?.let { "${it.toInt()}°C" } ?: "--°C"
                    val descText = weather.description?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    val humidityText = weather.humidity?.let { "${it}%" }
                    val pressureText = weather.pressure?.let { "${it} hPa" }
                    val windText = weather.windSpeed?.let { "${it} m/s" }

                    // Intentamos calcular la hora local del lugar consultado usando dt + timezone
                    if (weather.timestampUtcSeconds != null && weather.timezoneOffsetSeconds != null) {
                        // Guardamos el offset para casos fallback
                        timezoneOffsetSeconds = weather.timezoneOffsetSeconds


                        val days = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
                        val months = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")

                        // Calculamos la hora local en base al dt UTC y al offset recuperado
                        val tz = TimeZone.of(formatOffset(weather.timezoneOffsetSeconds))
                        val ldt = Instant.fromEpochSeconds(weather.timestampUtcSeconds).toLocalDateTime(tz)

                        val hh = ldt.hour.toString().padStart(2, '0')
                        val mm = ldt.minute.toString().padStart(2, '0')

                        val sec = ldt.second
                        val prev = ((sec + 59) % 60).toString().padStart(2, '0')
                        val cur = sec.toString().padStart(2, '0')
                        val next = ((sec + 1) % 60).toString().padStart(2, '0')

                        val dayName = days[ldt.dayOfWeek.ordinal]
                        val dateText = "${ldt.day} ${months[ldt.month.number - 1]}"

                        // Actualizamos ciudad, clima y reloj con la hora local remota
                        _state.value = _state.value.copy(
                            city = _state.value.city.copy(
                                text = if (cityText.isNotBlank()) cityText else _state.value.city.text,
                                isLoading = false,
                                isEmpty = false,
                                error = null
                            ),
                            weather = _state.value.weather.copy(
                                temperature = tempText,
                                description = descText,
                                humidity = humidityText,
                                pressure = pressureText,
                                windSpeed = windText,
                                isLoading = false,
                                isEmpty = false,
                                error = null
                            ),
                            time = _state.value.time.copy(
                                hours = hh,
                                minutes = mm,
                                isLoading = false,
                                isEmpty = false,
                                error = null
                            ),
                            date = _state.value.date.copy(
                                day = "$dayName,",
                                date = dateText,
                                isLoading = false,
                                isEmpty = false,
                                error = null
                            ),
                            seconds = _state.value.seconds.copy(
                                prev = prev,
                                current = cur,
                                next = next,
                                isLoading = false,
                                isEmpty = false,
                                error = null
                            ),
                            isLoading = false,
                            error = null
                        )

                        // No dependemos del reloj del sistema para la visualización; el hilo seguirá avanzando
                        // usando remoteBaseLocalEpochSeconds/remoteBaseSystemEpochMillis para mantener el reloj sincronizado.

                    } else {
                        // Si no tenemos dt/timezone reiniciamos el override y solo actualizamos ciudad y clima
                        timezoneOffsetSeconds = null
                        remoteBaseLocalEpochSeconds = null
                        remoteBaseSystemEpochMillis = null

                        _state.value = _state.value.copy(
                            city = _state.value.city.copy(
                                text = if (cityText.isNotBlank()) cityText else _state.value.city.text,
                                isLoading = false,
                                isEmpty = false,
                                error = null
                            ),
                            weather = _state.value.weather.copy(
                                temperature = tempText,
                                description = descText,
                                humidity = humidityText,
                                pressure = pressureText,
                                windSpeed = windText,
                                isLoading = false,
                                isEmpty = false,
                                error = null
                            ),
                            isLoading = false,
                            error = null
                        )
                    }

                }, onFailure = { throwable ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = throwable.message ?: "Error al obtener el clima",
                        city = _state.value.city.copy(isLoading = false, error = throwable.message),
                        weather = _state.value.weather.copy(isLoading = false, isEmpty = true, error = throwable.message)
                    )
                })
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error inesperado",
                    city = _state.value.city.copy(isLoading = false, error = e.message),
                    weather = _state.value.weather.copy(isLoading = false, isEmpty = true, error = e.message)
                )
            }
        }
    }

}