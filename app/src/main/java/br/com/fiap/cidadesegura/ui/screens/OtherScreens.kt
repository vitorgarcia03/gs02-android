package br.com.fiap.cidadesegura.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.fiap.cidadesegura.data.Cities
import br.com.fiap.cidadesegura.ui.CityViewModel

private val recommendations = listOf(
    "Antes de sair" to "Consulte a previsão e os alertas oficiais do seu município. Evite áreas que costumam alagar.",
    "Durante chuvas fortes" to "Não atravesse enxurradas, ruas inundadas ou pontes submersas. Procure abrigo seguro.",
    "Risco de deslizamentos" to "Observe rachaduras, inclinação de postes ou árvores e movimentação de terra. Afaste-se do local em caso de risco.",
    "Em uma emergência" to "No Brasil, ligue 199 (Defesa Civil) ou 193 (Corpo de Bombeiros), conforme a situação.",
    "Depois da chuva" to "Evite contato com água de enchente e acompanhe orientações das autoridades antes de retornar a locais atingidos."
)

@Composable
fun TipsScreen(onNavigate: (String) -> Unit) {
    MainLayout("Orientações de prevenção", "tips", onNavigate) { padding ->
        LazyColumn(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { Text("Dicas para enfrentar chuvas e alagamentos", style = MaterialTheme.typography.headlineSmall) }
            items(recommendations) { (title, body) ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(title, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(6.dp))
                        Text(body)
                    }
                }
            }
            item { Text("Conteúdo educativo. Em caso de perigo, siga as orientações da Defesa Civil local.", style = MaterialTheme.typography.bodySmall) }
        }
    }
}

@Composable
fun FavoritesScreen(vm: CityViewModel, onOpenCity: (String) -> Unit, onNavigate: (String) -> Unit) {
    val favorites by vm.favorites.collectAsState()
    val cities = Cities.all.filter { it.id in favorites }
    MainLayout("Cidades favoritas", "favorites", onNavigate) { padding ->
        LazyColumn(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item { Text("Acesso rápido às cidades salvas", style = MaterialTheme.typography.titleLarge) }
            if (cities.isEmpty()) item { Text("Você ainda não favoritou nenhuma cidade. Volte ao Início e toque no coração.") }
            items(cities, key = { it.id }) { city ->
                CityCard(city, true, onSelect = { onOpenCity(city.id) }, onFavorite = { vm.toggleFavorite(city.id) })
            }
        }
    }
}

@Composable
fun AboutScreen(onNavigate: (String) -> Unit) {
    MainLayout("Sobre o aplicativo", "about", onNavigate) { padding ->
        Column(Modifier.padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("CidadeSegura", style = MaterialTheme.typography.headlineMedium)
            Text("Uma proposta CitizenTech para facilitar o acesso a dados climáticos e a orientações de prevenção.")
            SectionTitle("Como funciona")
            Text("A previsão é consultada pela API pública Open-Meteo, usando Retrofit. As cidades favoritas e a escolha de não repetir a introdução ficam salvas localmente no aparelho.")
            SectionTitle("ODS relacionados")
            Text("ODS 11 — Cidades e comunidades sustentáveis; ODS 13 — Ação contra a mudança global do clima.")
            SectionTitle("Desenvolvedores")
            Text("Caíque De Lacerda Braga — RM 555532")
            Text("Vitor Garcia Correia — RM 558516")
            Text("Este MVP não possui sensores de risco nem emite alertas oficiais de emergência.", style = MaterialTheme.typography.bodySmall)
        }
    }
}
