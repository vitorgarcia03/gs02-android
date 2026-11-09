package br.com.fiap.cidadesegura.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private data class Tab(val route: String, val title: String)
private val tabs = listOf(
    Tab("home", "Início"), Tab("forecast", "Previsão"), Tab("tips", "Dicas"),
    Tab("favorites", "Salvos"), Tab("about", "Sobre")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(title: String, current: String, onNavigate: (String) -> Unit, content: @Composable (PaddingValues) -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(title) }) },
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    val icon = when(tab.route) {
                        "home" -> Icons.Default.Home
                        "forecast" -> Icons.Default.Cloud
                        "tips" -> Icons.Default.Info
                        "favorites" -> Icons.Default.Favorite
                        else -> Icons.Default.Person
                    }
                    NavigationBarItem(
                        selected = current == tab.route,
                        onClick = { if (current != tab.route) onNavigate(tab.route) },
                        label = { Text(tab.title) },
                        icon = { Icon(icon, contentDescription = tab.title) }
                    )
                }
            }
        }, content = content
    )
}

@Composable
fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(vertical = 8.dp))
}
