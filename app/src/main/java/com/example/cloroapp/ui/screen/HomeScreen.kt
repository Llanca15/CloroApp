package com.example.cloroapp.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cloroapp.ui.viewmodel.EstadoCloro
import com.example.cloroapp.ui.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsState()

    val colorTarjeta by animateColorAsState(
        targetValue = when (estado.estadoCloro) {
            EstadoCloro.NORMAL -> Color(0xFFE8F5E9)      // Verde suave
            EstadoCloro.ADVERTENCIA -> Color(0xFFFFF8E1) // Amarillo suave
            EstadoCloro.CRITICO -> Color(0xFFFFEBEE)     // Rojo suave
        },
        label = "colorTarjeta"
    )

    val colorTextoEstado by animateColorAsState(
        targetValue = when (estado.estadoCloro) {
            EstadoCloro.NORMAL -> Color(0xFF2E7D32)      // Verde oscuro
            EstadoCloro.ADVERTENCIA -> Color(0xFFF57F17) // Naranja/Amarillo oscuro
            EstadoCloro.CRITICO -> Color(0xFFC62828)     // Rojo oscuro
        },
        label = "colorTextoEstado"
    )

    val escalaTitulo by animateFloatAsState(
        targetValue = if (estado.modoMonitoreoIntensivo) 1.08f else 1.0f,
        label = "escalaTitulo"
    )

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Medición de Cloro en Línea",
                modifier = Modifier.graphicsLayer {
                    scaleX = escalaTitulo
                    scaleY = escalaTitulo
                },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Control de Calidad de Agua en Granjas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = estado.nombreOperario,
                onValueChange = viewModel::cambiarNombreOperario,
                label = { Text("Operario / Responsable") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = estado.granjaPunto,
                onValueChange = viewModel::cambiarGranjaPunto,
                label = { Text("Granja / Galpón de Medición") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorTarjeta
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Nivel de Cloro Libre",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "%.1f ppm".format(estado.nivelCloroPpm),
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorTextoEstado
                    )

                    Text(
                        text = "Estado: ${estado.estadoCloro.name}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = colorTextoEstado
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = estado.mensajeEstado,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = colorTextoEstado
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = viewModel::decrementarPpm,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("- 0.1", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = viewModel::incrementarPpm,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("+ 0.1", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = viewModel::registrarMedicion,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorTextoEstado
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Registrar Control en Base de Datos", color = Color.White)
                    }
                }
            }

            OutlinedTextField(
                value = estado.rangoIdealPpm,
                onValueChange = viewModel::cambiarRangoIdeal,
                label = { Text("Rango Ideal Objetivo (ppm)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Monitoreo Intensivo de Alertas",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (estado.modoMonitoreoIntensivo)
                                "Modo activo: notificaciones prioritarias"
                            else
                                "Modo estándar",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Switch(
                        checked = estado.modoMonitoreoIntensivo,
                        onCheckedChange = {
                            viewModel.cambiarModoMonitoreoIntensivo()
                        }
                    )
                }
            }

            Text(
                text = "Controles registrados hoy: ${estado.controlesRealizadosHoy}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
