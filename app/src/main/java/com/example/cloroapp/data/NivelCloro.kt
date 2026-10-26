package com.example.cloroapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "niveles_cloro",
    foreignKeys = [
        ForeignKey(
            entity = PuntoMedicion::class,
            parentColumns = ["id"],
            childColumns = ["puntoMedicionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["puntoMedicionId"])]
)
data class NivelCloro(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val puntoMedicionId: Int,
    val valorPpm: Double, // Nivel de cloro libre en ppm
    val fechaHora: String,
    val estado: String, // "NORMAL", "ADVERTENCIA", "CRITICO"
    val registradoPor: String = "Operario"
)
