package com.example.cloroapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "alertas",
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
data class Alerta(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val puntoMedicionId: Int,
    val titulo: String,
    val descripcion: String,
    val nivelCloro: Double,
    val fechaHora: String,
    val resuelta: Boolean = false,
    val accionCorrectiva: String? = null
)
