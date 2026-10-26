package com.example.cloroapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Usuario::class,
        Granja::class,
        PuntoMedicion::class,
        NivelCloro::class,
        Alerta::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun granjaDao(): GranjaDao
    abstract fun puntoMedicionDao(): PuntoMedicionDao
    abstract fun nivelCloroDao(): NivelCloroDao
    abstract fun alertaDao(): AlertaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cloro_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
