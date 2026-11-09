package br.com.fiap.cidadesegura.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.cidadesegura.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface ForecastState {
    data object Initial : ForecastState
    data object Loading : ForecastState
    data class Success(val days: List<ForecastDay>) : ForecastState
    data class Error(val message: String) : ForecastState
}

class CityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CityRepository()
    private val preferences = PreferencesRepository(application)
    private val _forecast = MutableStateFlow<ForecastState>(ForecastState.Initial)
    val forecast: StateFlow<ForecastState> = _forecast
    private val _favorites = MutableStateFlow(preferences.favorites())
    val favorites: StateFlow<Set<String>> = _favorites
    private val _selected = MutableStateFlow(Cities.all.first())
    val selected: StateFlow<City> = _selected

    fun chooseCity(city: City) {
        _selected.value = city
        loadForecast()
    }
    fun loadForecast() {
        val city = _selected.value
        viewModelScope.launch {
            _forecast.value = ForecastState.Loading
            try {
                val days = repository.forecast(city)
                _forecast.value = if (days.isEmpty()) ForecastState.Error("API não retornou dias de previsão.")
                                  else ForecastState.Success(days)
            } catch (e: Exception) {
                _forecast.value = ForecastState.Error("Não foi possível consultar a previsão. Verifique sua conexão e tente novamente.")
            }
        }
    }
    fun toggleFavorite(cityId: String) { _favorites.value = preferences.toggleFavorite(cityId) }
}
