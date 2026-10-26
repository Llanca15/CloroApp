package com.example.cloroapp.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "preferencias_cloro")

class PreferenciasRepository(private val context: Context) {

    companion object {
        private val OPERARIO = stringPreferencesKey("nombre_operario")
        private val GRANJA_PUNTO = stringPreferencesKey("granja_punto")
        private val RANGO_IDEAL = stringPreferencesKey("rango_ideal_ppm")
        private val MONITOREO_INTENSIVO = booleanPreferencesKey("monitoreo_intensivo")
    }

    val nombreOperario: Flow<String> = context.dataStore.data.map { pref ->
        pref[OPERARIO] ?: "Operario Terreno"
    }

    val granjaPunto: Flow<String> = context.dataStore.data.map { pref ->
        pref[GRANJA_PUNTO] ?: "Granja Melipilla - Galpón 1"
    }

    val rangoIdealPpm: Flow<String> = context.dataStore.data.map { pref ->
        pref[RANGO_IDEAL] ?: "1.5"
    }

    val modoMonitoreoIntensivo: Flow<Boolean> = context.dataStore.data.map { pref ->
        pref[MONITOREO_INTENSIVO] ?: false
    }

    suspend fun guardarNombreOperario(nombre: String) {
        context.dataStore.edit { pref ->
            pref[OPERARIO] = nombre
        }
    }

    suspend fun guardarGranjaPunto(granjaPunto: String) {
        context.dataStore.edit { pref ->
            pref[GRANJA_PUNTO] = granjaPunto
        }
    }

    suspend fun guardarRangoIdeal(rango: String) {
        context.dataStore.edit { pref ->
            pref[RANGO_IDEAL] = rango
        }
    }

    suspend fun guardarMonitoreoIntensivo(activado: Boolean) {
        context.dataStore.edit { pref ->
            pref[MONITOREO_INTENSIVO] = activado
        }
    }
}
