package br.com.fiap.cidadesegura.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

/** API pública Open-Meteo: https://open-meteo.com/en/docs */
interface WeatherApi {
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("daily") daily: String = "precipitation_sum,precipitation_probability_max,temperature_2m_max,weather_code",
        @Query("timezone") timezone: String = "America/Sao_Paulo",
        @Query("forecast_days") days: Int = 5
    ): WeatherResponse
}

data class WeatherResponse(val daily: DailyWeather)

data class DailyWeather(
    val time: List<String> = emptyList(),
    @SerializedName("precipitation_sum") val rainMm: List<Double?> = emptyList(),
    @SerializedName("precipitation_probability_max") val rainChance: List<Int?> = emptyList(),
    @SerializedName("temperature_2m_max") val maxTemp: List<Double?> = emptyList(),
    @SerializedName("weather_code") val weatherCode: List<Int?> = emptyList()
)

data class ForecastDay(
    val date: String,
    val rainMm: Double,
    val chance: Int,
    val temp: Double,
    val weatherCode: Int
) {
    val guidance: String get() = when {
        rainMm >= 30 || chance >= 85 -> "Atenção à chuva: acompanhe os avisos oficiais."
        rainMm >= 10 || chance >= 60 -> "Possibilidade de chuva. Planeje seus deslocamentos."
        else -> "Sem indicação de chuva intensa nesta previsão."
    }
}
