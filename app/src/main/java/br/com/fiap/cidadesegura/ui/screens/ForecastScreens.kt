package br.com.fiap.cidadesegura.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.fiap.cidadesegura.data.Cities
import br.com.fiap.cidadesegura.data.City
import br.com.fiap.cidadesegura.data.ForecastDay
import br.com.fiap.cidadesegura.ui.CityViewModel
import br.com.fiap.cidadesegura.ui.ForecastState
import java.util.Locale

@Composable
fun ForecastScreen(vm: CityViewModel, onNavigate: (String) -> Unit) {
    val selected by vm.selected.collectAsState()
    MainLayout("Previsão do tempo", "forecast", onNavigate) { padding ->
        ForecastContent(vm, selected, Modifier.padding(padding), showSelector = true)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(city: City, vm: CityViewModel, onBack: () -> Unit) {
    LaunchedEffect(city.id) { vm.chooseCity(city) }
    Scaffold(topBar = {
        TopAppBar(title = { Text(city.name) }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Voltar") }
        })
    }) { padding -> ForecastContent(vm, city, Modifier.padding(padding), showSelector = false) }
}

@Composable
private fun ForecastContent(vm: CityViewModel, city: City, modifier: Modifier = Modifier, showSelector: Boolean) {
    val state by vm.forecast.collectAsState()
    var dropdown by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { if (state is ForecastState.Initial) vm.loadForecast() }
    Column(modifier.fillMaxSize().padding(16.dp)) {
        if (showSelector) {
            Box {
                OutlinedButton(onClick = { dropdown = true }) { Text("Cidade: ${city.name} ▾") }
                DropdownMenu(expanded = dropdown, onDismissRequest = { dropdown = false }) {
                    Cities.all.forEach { option ->
                        DropdownMenuItem(text = { Text("${option.name} - ${option.state}") }, onClick = {
                            dropdown = false
                            vm.chooseCity(option)
                        })
                    }
                }
            }
        } else {
            Text("${city.name} - ${city.state}", style = MaterialTheme.typography.headlineSmall)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Próximos 5 dias", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            IconButton(onClick = { vm.loadForecast() }) { Icon(Icons.Default.Refresh, contentDescription = "Atualizar") }
        }
        when (val s = state) {
            is ForecastState.Initial -> Text("Selecione uma cidade para consultar.")
            is ForecastState.Loading -> Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is ForecastState.Error -> Column {
                Text(s.message, color = MaterialTheme.colorScheme.error)
                Button(onClick = { vm.loadForecast() }) { Text("Tentar novamente") }
            }
            is ForecastState.Success -> LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(s.days, key = { it.date }) { ForecastCard(it) }
                item { Text("Fonte: Open-Meteo. Probabilidade e acumulado são estimativas, não avisos oficiais.", style = MaterialTheme.typography.bodySmall) }
            }
        }
    }
}

@Composable
private fun ForecastCard(day: ForecastDay) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(day.date, style = MaterialTheme.typography.titleMedium)
            Text("Chuva prevista: ${String.format(Locale("pt", "BR"), "%.1f", day.rainMm)} mm")
            Text("Probabilidade máxima de chuva: ${day.chance}%")
            Text("Temperatura máxima: ${String.format(Locale("pt", "BR"), "%.1f", day.temp)} °C")
            Text(day.guidance, color = MaterialTheme.colorScheme.primary)
        }
    }
}
