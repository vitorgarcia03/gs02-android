package br.com.fiap.cidadesegura.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.fiap.cidadesegura.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    LaunchedEffect(Unit) { delay(1400); onFinish() }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Image(painterResource(R.drawable.ic_app_logo), contentDescription = "Logo CidadeSegura", modifier = Modifier.size(120.dp))
            Text("CidadeSegura", style = MaterialTheme.typography.headlineLarge)
            Text("Informação climática para sua cidade")
            CircularProgressIndicator()
        }
    }
}

private data class IntroItem(val title: String, val subtitle: String)
private val items = listOf(
    IntroItem("Sua cidade, sua segurança", "Consulte informações de chuva e temperatura para organizar sua rotina."),
    IntroItem("Previsão em tempo real", "Dados públicos atualizados de uma API REST com dias de previsão."),
    IntroItem("Informação que ajuda", "Favorite cidades e veja recomendações de prevenção. Em emergências, siga a Defesa Civil.")
)

@Composable
fun IntroScreen(onFinish: (Boolean) -> Unit) {
    var page by remember { mutableIntStateOf(0) }
    var rememberChoice by remember { mutableStateOf(true) }
    val item = items[page]
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painterResource(R.drawable.ic_app_logo), contentDescription = "Ícone de proteção", modifier = Modifier.size(112.dp))
        Spacer(Modifier.height(24.dp))
        Text("${page + 1} de ${items.size}", color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(12.dp))
        Text(item.title, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(12.dp))
        Text(item.subtitle, textAlign = TextAlign.Center)
        Spacer(Modifier.height(28.dp))
        if (page == items.lastIndex) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = rememberChoice, onCheckedChange = { rememberChoice = it })
                Text("Não mostrar novamente")
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = { page-- }, enabled = page > 0, modifier = Modifier.weight(1f)) { Text("Voltar") }
            Button(onClick = { if (page < items.lastIndex) page++ else onFinish(rememberChoice) }, modifier = Modifier.weight(1f)) {
                Text(if (page == items.lastIndex) "Começar" else "Avançar")
            }
        }
    }
}
