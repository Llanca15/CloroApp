package com.example.cloroapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios")
    fun obtenerUsuarios(): Flow<List<Usuario>>

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun obtenerPorCorreo(correo: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario)
}

@Dao
interface GranjaDao {
    @Query("SELECT * FROM granjas")
    fun obtenerGranjas(): Flow<List<Granja>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(granja: Granja)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(granjas: List<Granja>)
}

@Dao
interface PuntoMedicionDao {
    @Query("SELECT * FROM puntos_medicion WHERE granjaId = :granjaId")
    fun obtenerPuntosPorGranja(granjaId: Int): Flow<List<PuntoMedicion>>

    @Query("SELECT * FROM puntos_medicion")
    fun obtenerTodosPuntos(): Flow<List<PuntoMedicion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(punto: PuntoMedicion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(puntos: List<PuntoMedicion>)
}

@Dao
interface NivelCloroDao {
    @Query("SELECT * FROM niveles_cloro WHERE puntoMedicionId = :puntoId ORDER BY fechaHora DESC")
    fun obtenerMedicionesPorPunto(puntoId: Int): Flow<List<NivelCloro>>

    @Query("SELECT * FROM niveles_cloro ORDER BY fechaHora DESC")
    fun obtenerTodasMediciones(): Flow<List<NivelCloro>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(medicion: NivelCloro)
}

@Dao
interface AlertaDao {
    @Query("SELECT * FROM alertas ORDER BY fechaHora DESC")
    fun obtenerTodasAlertas(): Flow<List<Alerta>>

    @Query("SELECT * FROM alertas WHERE resuelta = 0 ORDER BY fechaHora DESC")
    fun obtenerAlertasActivas(): Flow<List<Alerta>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(alerta: Alerta)

    @Update
    suspend fun actualizar(alerta: Alerta)
}
