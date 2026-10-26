package com.example.cloroapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloroapp.data.PreferenciasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = PreferenciasRepository(application)
    private val contador = MutableStateFlow(0)

    val uiState: StateFlow<MainUiState> = combine(
        repositorio.nombre,
        repositorio.modoConcentracion,
        repositorio.metaDiaria,
        contador
    ) { nombre, modo, meta, valor ->
        MainUiState(
            nombre = nombre,
            contador = valor,
            modoConcentracion = modo,
            metaDiaria = meta
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainUiState()
    )

    fun cambiarNombre(nombre: String) {
        viewModelScope.launch {
            repositorio.guardarNombre(nombre)
        }
    }

    fun cambiarMetaDiaria(meta: String) {
        viewModelScope.launch {
            repositorio.guardarMetaDiaria(meta)
        }
    }

    fun incrementar() {
        contador.value++
    }

    fun decrementar() {
        if (contador.value > 0) {
            contador.value--
        }
    }

    fun cambiarModoConcentracion() {
        val nuevoValor = !uiState.value.modoConcentracion
        viewModelScope.launch {
            repositorio.guardarModoConcentracion(nuevoValor)
        }
    }
}
