package br.com.fiap.cidadesegura.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class City(val id: String, val name: String, val state: String, val latitude: Double, val longitude: Double)

object Cities {
    val all = listOf(
        City("sp", "São Paulo", "SP", -23.5505, -46.6333),
        City("campinas", "Campinas", "SP", -22.9056, -47.0608),
        City("santos", "Santos", "SP", -23.9608, -46.3336),
        City("curitiba", "Curitiba", "PR", -25.4284, -49.2733),
        City("bh", "Belo Horizonte", "MG", -19.9167, -43.9345)
    )
    fun byId(id: String): City? = all.find { it.id == id }
}

class CityRepository {
    private val api = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)

    suspend fun forecast(city: City): List<ForecastDay> {
        val daily = api.getForecast(city.latitude, city.longitude).daily
        return daily.time.indices.map { i ->
            ForecastDay(
                date = daily.time[i],
                rainMm = daily.rainMm.getOrNull(i) ?: 0.0,
                chance = daily.rainChance.getOrNull(i) ?: 0,
                temp = daily.maxTemp.getOrNull(i) ?: 0.0,
                weatherCode = daily.weatherCode.getOrNull(i) ?: 0
            )
        }
    }
}
