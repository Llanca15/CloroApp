package com.example.cloroapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "puntos_medicion",
    foreignKeys = [
        ForeignKey(
            entity = Granja::class,
            parentColumns = ["id"],
            childColumns = ["granjaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["granjaId"])]
)
data class PuntoMedicion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val granjaId: Int,
    val nombrePunto: String, // ej: "Galpón 1", "Entrada de Agua"
    val descripcion: String = ""
)
