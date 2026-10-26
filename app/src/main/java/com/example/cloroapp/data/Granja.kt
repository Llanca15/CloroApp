package com.example.cloroapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "granjas")
data class Granja(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val ubicacion: String
)
