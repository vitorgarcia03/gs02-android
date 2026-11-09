package br.com.fiap.cidadesegura.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.fiap.cidadesegura.data.Cities
import br.com.fiap.cidadesegura.data.City
import br.com.fiap.cidadesegura.ui.CityViewModel

@Composable
fun HomeScreen(vm: CityViewModel, onOpenCity: (String) -> Unit, onNavigate: (String) -> Unit) {
    var search by remember { mutableStateOf("") }
    val favorites by vm.favorites.collectAsState()
    MainLayout("CidadeSegura", "home", onNavigate) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item {
                Spacer(Modifier.height(8.dp))
                Text("Previsão local e prevenção", style = MaterialTheme.typography.headlineSmall)
                Text("Selecione uma cidade para ver dados climáticos atualizados.")
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(value = search, onValueChange = { search = it }, label = { Text("Buscar cidade") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                SectionTitle("Cidades disponíveis")
            }
            items(Cities.all.filter { it.name.contains(search, ignoreCase = true) || it.state.contains(search, ignoreCase = true) }, key = { it.id }) { city ->
                CityCard(city, favorites.contains(city.id), onSelect = { onOpenCity(city.id) }, onFavorite = { vm.toggleFavorite(city.id) })
            }
            item { Spacer(Modifier.height(12.dp)); Text("As previsões não substituem alertas oficiais da Defesa Civil.", style = MaterialTheme.typography.bodySmall) }
        }
    }
}

@Composable
fun CityCard(city: City, isFavorite: Boolean, onSelect: () -> Unit, onFavorite: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Place, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column(Modifier.weight(1f).clickable { onSelect() }.padding(horizontal = 12.dp)) {
                Text(city.name, style = MaterialTheme.typography.titleMedium)
                Text(city.state, style = MaterialTheme.typography.bodySmall)
                Text("Ver previsão", color = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onFavorite) {
                Icon(if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = if(isFavorite) "Remover favorito" else "Favoritar cidade", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
