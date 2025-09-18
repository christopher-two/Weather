package org.christophertwo.weather.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.PaletteStyle
import org.jetbrains.compose.resources.Font
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.montserrat_medium

@Composable
fun WeatherTheme(content: @Composable () -> Unit) {
    DynamicMaterialTheme(
        seedColor = Color(0xFF000000),
        content = content,
        isAmoled = true,
        style = PaletteStyle.Monochrome,
        typography = Typography(
            displayMedium = Typography().displayMedium.copy(
                fontFamily = FontFamily(Font(Res.font.montserrat_medium)),
            ),
            displaySmall = Typography().displaySmall.copy(
                fontFamily = FontFamily(Font(Res.font.montserrat_medium)),
            ),
            headlineLarge = Typography().headlineLarge.copy(
                fontFamily = FontFamily(Font(Res.font.montserrat_medium)),
            ),
            headlineMedium = Typography().headlineMedium.copy(
                fontFamily = FontFamily(Font(Res.font.montserrat_medium)),
            ),
            headlineSmall = Typography().headlineSmall.copy(
                fontFamily = FontFamily(Font(Res.font.montserrat_medium)),
            ),
            titleLarge = Typography().titleLarge.copy(
                fontFamily = FontFamily(Font(Res.font.montserrat_medium)),
            ),
            titleMedium = Typography().titleMedium.copy(
                fontFamily = FontFamily(Font(Res.font.montserrat_medium)),
            ),
            titleSmall = Typography().titleSmall.copy(
                fontFamily = FontFamily(Font(Res.font.montserrat_medium)),
            ),
            bodyLarge = Typography().bodyLarge.copy(
                fontFamily = FontFamily(Font(Res.font.montserrat_medium)),
            ),
            bodyMedium = Typography().bodyMedium.copy(
                fontFamily = FontFamily(Font(Res.font.montserrat_medium)),
            ),
            bodySmall = Typography().bodySmall.copy(
                fontFamily = FontFamily(Font(Res.font.montserrat_medium)),
            ),
            labelLarge = Typography().labelLarge.copy(
                fontFamily = FontFamily(Font(Res.font.montserrat_medium)),
            ),
            labelMedium = Typography().labelMedium.copy(
                fontFamily = FontFamily(Font(Res.font.montserrat_medium)),
            ),
            labelSmall = Typography().labelSmall.copy(
                fontFamily = FontFamily(Font(Res.font.montserrat_medium)),
            ),
        )
    )
}