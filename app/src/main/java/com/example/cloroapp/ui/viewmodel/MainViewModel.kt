package com.example.cloroapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloroapp.data.AppDatabase
import com.example.cloroapp.data.NivelCloro
import com.example.cloroapp.data.PreferenciasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = PreferenciasRepository(application)
    private val database = AppDatabase.getDatabase(application)

    private val nivelCloroFlow = MutableStateFlow(1.5)
    private val controlesContadorFlow = MutableStateFlow(0)

    val uiState: StateFlow<MainUiState> = combine(
        repositorio.nombreOperario,
        repositorio.granjaPunto,
        repositorio.rangoIdealPpm,
        repositorio.modoMonitoreoIntensivo,
        combine(nivelCloroFlow, controlesContadorFlow) { c, cnt -> c to cnt }
    ) { nombre, granja, rango, modoIntensivo, (cloroPpm, contador) ->

        val (estado, mensaje) = calcularEstadoCloro(cloroPpm)

        MainUiState(
            nombreOperario = nombre,
            granjaPunto = granja,
            nivelCloroPpm = cloroPpm,
            rangoIdealPpm = rango,
            modoMonitoreoIntensivo = modoIntensivo,
            controlesRealizadosHoy = contador,
            estadoCloro = estado,
            mensajeEstado = mensaje
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainUiState()
    )

    private fun calcularEstadoCloro(ppm: Double): Pair<EstadoCloro, String> {
        return when {
            ppm in 1.0..2.0 -> {
                EstadoCloro.NORMAL to "Nivel óptimo para red de agua (1.0 - 2.0 ppm)"
            }
            (ppm in 0.5..<1.0) || (ppm in 2.01..2.5) -> {
                EstadoCloro.ADVERTENCIA to "Atención: Nivel fuera del rango ideal. Monitorear de cerca."
            }
            else -> {
                EstadoCloro.CRITICO to "¡ALERTA CRÍTICA! Cloro fuera de rangos de seguridad. Aplicar acción correctiva."
            }
        }
    }

    fun cambiarNombreOperario(nombre: String) {
        viewModelScope.launch {
            repositorio.guardarNombreOperario(nombre)
        }
    }

    fun cambiarGranjaPunto(granjaPunto: String) {
        viewModelScope.launch {
            repositorio.guardarGranjaPunto(granjaPunto)
        }
    }

    fun cambiarRangoIdeal(rango: String) {
        viewModelScope.launch {
            repositorio.guardarRangoIdeal(rango)
        }
    }

    fun cambiarModoMonitoreoIntensivo() {
        val nuevoValor = !uiState.value.modoMonitoreoIntensivo
        viewModelScope.launch {
            repositorio.guardarMonitoreoIntensivo(nuevoValor)
        }
    }

    fun incrementarPpm() {
        val actual = nivelCloroFlow.value
        val nuevo = ((actual + 0.1) * 10).roundToInt() / 10.0
        if (nuevo <= 5.0) {
            nivelCloroFlow.value = nuevo
        }
    }

    fun decrementarPpm() {
        val actual = nivelCloroFlow.value
        val nuevo = ((actual - 0.1) * 10).roundToInt() / 10.0
        if (nuevo >= 0.0) {
            nivelCloroFlow.value = nuevo
        }
    }

    fun registrarMedicion() {
        viewModelScope.launch {
            val currentState = uiState.value
            val fechaActual = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

            val nuevaMedicion = NivelCloro(
                puntoMedicionId = 1,
                valorPpm = currentState.nivelCloroPpm,
                fechaHora = fechaActual,
                estado = currentState.estadoCloro.name,
                registradoPor = currentState.nombreOperario
            )

            database.nivelCloroDao().insertar(nuevaMedicion)
            controlesContadorFlow.value++
        }
    }
}
