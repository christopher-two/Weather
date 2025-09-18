package org.christophertwo.weather.presentation.features.main

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp
import androidx.compose.runtime.Composable

// Tokens centralizados para tamaños responsive.
data class SizeTokens(
    val timeFont: TextUnit,
    val timeLine: TextUnit,
    val dateFont: TextUnit,
    val secondsFont: TextUnit,
    val bigBoxH: Dp,
    val bigBoxW: Dp,
    val spacerSmall: Dp,
    val spacerMedium: Dp,
    val smallH: Dp,
    val smallW1: Dp,
    val smallW2: Dp,
    val offsetX: Dp,
    val offsetY: Dp,
    val secFont: TextUnit,
    val cityFont: TextUnit,
    val cityLine: TextUnit,
    val placeholderH: Dp,
    val placeholderGap: Dp,
    val sunFont: TextUnit
) {
    companion object {
        @Composable
        fun of(compact: Boolean): SizeTokens = if (compact) {
            SizeTokens(
                timeFont = 66.ssp,
                timeLine = 68.ssp,
                dateFont = 16.ssp,
                secondsFont = 20.ssp,
                bigBoxH = 72.sdp,
                bigBoxW = 64.sdp,
                spacerSmall = 6.sdp,
                spacerMedium = 12.sdp,
                smallH = 20.sdp,
                smallW1 = 48.sdp,
                smallW2 = 56.sdp,
                offsetX = 28.sdp,
                offsetY = 14.sdp,
                secFont = 20.ssp,
                cityFont = 26.ssp,
                cityLine = 34.ssp,
                placeholderH = 40.sdp,
                placeholderGap = 6.sdp,
                sunFont = 12.ssp
            )
        } else {
            SizeTokens(
                timeFont = 96.ssp,
                timeLine = 90.ssp,
                dateFont = 28.ssp,
                secondsFont = 32.ssp,
                bigBoxH = 96.sdp,
                bigBoxW = 80.sdp,
                spacerSmall = 8.sdp,
                spacerMedium = 16.sdp,
                smallH = 28.sdp,
                smallW1 = 64.sdp,
                smallW2 = 72.sdp,
                offsetX = 36.sdp,
                offsetY = 18.sdp,
                secFont = 32.ssp,
                cityFont = 40.ssp,
                cityLine = 44.ssp,
                placeholderH = 48.sdp,
                placeholderGap = 8.sdp,
                sunFont = 18.ssp
            )
        }
    }
}

