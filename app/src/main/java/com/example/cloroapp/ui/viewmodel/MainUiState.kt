package com.example.cloroapp.ui.viewmodel

enum class EstadoCloro {
    NORMAL,      // 1.0 - 2.0 ppm (Apto)
    ADVERTENCIA, // 0.5 - 0.9 ppm o 2.1 - 2.5 ppm (Revisión recomendada)
    CRITICO      // < 0.5 ppm o > 2.5 ppm (Acción inmediata)
}

data class MainUiState(
    val nombreOperario: String = "Operario Terreno",
    val granjaPunto: String = "Granja Melipilla - Galpón 1",
    val nivelCloroPpm: Double = 1.5,
    val rangoIdealPpm: String = "1.5",
    val modoMonitoreoIntensivo: Boolean = false,
    val controlesRealizadosHoy: Int = 0,
    val estadoCloro: EstadoCloro = EstadoCloro.NORMAL,
    val mensajeEstado: String = "Nivel óptimo para red de agua (1.0 - 2.0 ppm)",
    val ultimoRegistroExitoso: Boolean = false
)
