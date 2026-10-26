package com.example.cloroapp.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cloroapp.ui.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsState()

    val colorTarjeta by animateColorAsState(
        targetValue = if (estado.modoConcentracion) {
            Color(0xFFDDF7E8)
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        label = "colorTarjeta"
    )

    val escalaTitulo by animateFloatAsState(
        targetValue = if (estado.modoConcentracion) {
            1.08f
        } else {
            1f
        },
        label = "escalaTitulo"
    )

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Mi Espacio Personal",
                modifier = Modifier.graphicsLayer {
                    scaleX = escalaTitulo
                    scaleY = escalaTitulo
                },
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                value = estado.nombre,
                onValueChange = viewModel::cambiarNombre,
                label = { Text("Tu nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text(
                text = if (estado.nombre.isBlank()) {
                    "¡Bienvenido!"
                } else {
                    "¡Hola, ${estado.nombre}!"
                },
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = estado.metaDiaria,
                onValueChange = viewModel::cambiarMetaDiaria,
                label = { Text("Meta diaria de mediciones") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (estado.metaDiaria.isNotBlank()) {
                Text(
                    text = "Meta propuesta: ${estado.metaDiaria}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorTarjeta
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Contador de Mediciones",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${estado.contador}",
                        style = MaterialTheme.typography.displayMedium
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(onClick = viewModel::decrementar) {
                            Text("-")
                        }
                        Button(onClick = viewModel::incrementar) {
                            Text("+")
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Modo de concentración",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = estado.modoConcentracion,
                    onCheckedChange = {
                        viewModel.cambiarModoConcentracion()
                    }
                )
            }

            Text(
                text = if (estado.modoConcentracion) {
                    "Modo activado 🎯 (Visualización optimizada)"
                } else {
                    "Modo desactivado"
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
