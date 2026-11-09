package br.com.fiap.cidadesegura.data

import android.content.Context

/** Persistência local de onboarding e municípios favoritos. */
class PreferencesRepository(context: Context) {
    private val prefs = context.getSharedPreferences("cidadesegura_prefs", Context.MODE_PRIVATE)
    fun skipOnboarding() = prefs.getBoolean("onboarding_done", false)
    fun markOnboardingComplete() = prefs.edit().putBoolean("onboarding_done", true).apply()
    fun favorites(): Set<String> = prefs.getStringSet("favorite_cities", emptySet())?.toSet() ?: emptySet()
    fun toggleFavorite(id: String): Set<String> {
        val updated = favorites().toMutableSet().apply { if (!add(id)) remove(id) }
        prefs.edit().putStringSet("favorite_cities", updated).apply()
        return updated.toSet()
    }
}
