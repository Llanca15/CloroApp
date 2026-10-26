package com.example.cloroapp.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "preferencias_usuario")

class PreferenciasRepository(private val context: Context) {

    companion object {
        private val NOMBRE = stringPreferencesKey("nombre_usuario")
        private val MODO = booleanPreferencesKey("modo_concentracion")
        private val META = stringPreferencesKey("meta_diaria")
    }

    val nombre: Flow<String> = context.dataStore.data.map { pref ->
        pref[NOMBRE] ?: ""
    }

    val modoConcentracion: Flow<Boolean> = context.dataStore.data.map { pref ->
        pref[MODO] ?: false
    }

    val metaDiaria: Flow<String> = context.dataStore.data.map { pref ->
        pref[META] ?: ""
    }

    suspend fun guardarNombre(nombre: String) {
        context.dataStore.edit { pref ->
            pref[NOMBRE] = nombre
        }
    }

    suspend fun guardarModoConcentracion(activado: Boolean) {
        context.dataStore.edit { pref ->
            pref[MODO] = activado
        }
    }

    suspend fun guardarMetaDiaria(meta: String) {
        context.dataStore.edit { pref ->
            pref[META] = meta
        }
    }
}
