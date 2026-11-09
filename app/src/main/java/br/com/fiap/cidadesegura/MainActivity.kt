package br.com.fiap.cidadesegura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.fiap.cidadesegura.data.Cities
import br.com.fiap.cidadesegura.data.PreferencesRepository
import br.com.fiap.cidadesegura.ui.CityViewModel
import br.com.fiap.cidadesegura.ui.screens.*
import br.com.fiap.cidadesegura.ui.theme.CidadeSeguraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CidadeSeguraTheme { CidadeSeguraApp() } }
    }
}

@Composable
fun CidadeSeguraApp() {
    val nav = rememberNavController()
    val context = LocalContext.current
    val prefs = remember(context) { PreferencesRepository(context) }
    val vm: CityViewModel = viewModel()

    NavHost(navController = nav, startDestination = "splash") {
        composable("splash") {
            SplashScreen {
                val dest = if (prefs.skipOnboarding()) "home" else "intro"
                nav.navigate(dest) { popUpTo("splash") { inclusive = true } }
            }
        }
        composable("intro") {
            IntroScreen(onFinish = { rememberChoice ->
                if (rememberChoice) prefs.markOnboardingComplete()
                nav.navigate("home") { popUpTo("intro") { inclusive = true } }
            })
        }
        composable("home") { HomeScreen(vm, onOpenCity = { nav.navigate("detail/$it") }, onNavigate = { nav.navigate(it) }) }
        composable("forecast") { ForecastScreen(vm, onNavigate = { nav.navigate(it) }) }
        composable("tips") { TipsScreen(onNavigate = { nav.navigate(it) }) }
        composable("favorites") { FavoritesScreen(vm, onOpenCity = { nav.navigate("detail/$it") }, onNavigate = { nav.navigate(it) }) }
        composable("about") { AboutScreen(onNavigate = { nav.navigate(it) }) }
        composable("detail/{cityId}", arguments = listOf(navArgument("cityId") { type = NavType.StringType })) { entry ->
            val city = Cities.byId(entry.arguments?.getString("cityId").orEmpty())
            if (city != null) DetailScreen(city, vm, onBack = { nav.popBackStack() })
        }
    }
}
