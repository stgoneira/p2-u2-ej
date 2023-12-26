package cl.stgoneira.android.p2_u2_ej3.data

import android.util.Log
import cl.stgoneira.android.p2_u2_ej3.data.modelo.Tarea

class TareaMemoryDataSource {
    private val _tareas = mutableListOf<Tarea>()

    init {
        _tareas.addAll(tareasDePrueba())
    }

    fun obtenerTodas():List<Tarea> {
        return _tareas
    }

    fun insertar(vararg tareas: Tarea) {
        _tareas.addAll( tareas.asList() )
    }

    fun eliminar(tarea: Tarea) {
        _tareas.remove(tarea)
        Log.v("DataSource", _tareas.toString())
    }

    private fun tareasDePrueba():List<Tarea> = listOf(
        /*
        Tarea(UUID.randomUUID().toString(), "Lavar la ropa")
        , Tarea(UUID.randomUUID().toString(), "Cocinar")
        , Tarea(UUID.randomUUID().toString(), "Supermercado")
        , Tarea(System.currentTimeMillis(), "Veterinario Rocky")
        , Tarea(System.currentTimeMillis(), "Terminar Taller 1")
        , Tarea(System.currentTimeMillis(), "Matrícula")
        , Tarea(System.currentTimeMillis(), "Comprar pasajes avión")
        , Tarea(System.currentTimeMillis(), "Pagar cuentas")
        , Tarea(System.currentTimeMillis(), "Pagar Tarjetas de Crédito")
        , Tarea(System.currentTimeMillis(), "Reclamo comisiones")
        , Tarea(System.currentTimeMillis(), "Comprar regalos")
        , Tarea(System.currentTimeMillis(), "Veterinario Mila")
        , Tarea(System.currentTimeMillis(), "Informes de Cierre")
        , Tarea(System.currentTimeMillis(), "Preparar declaración de impuestos")
        , Tarea(System.currentTimeMillis(), "Pago a proveedores")
        , Tarea(System.currentTimeMillis(), "Renovación de arriendo")
        , Tarea(System.currentTimeMillis(), "Revisión Técnica")
         */
    )
}