package cl.stgoneira.android.p2_u2_ej3.data

import cl.stgoneira.android.p2_u2_ej3.data.modelo.Tarea
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.FileInputStream
import java.io.FileOutputStream

class TareasRepository(
    private val tareaMemoryDataSource: TareaMemoryDataSource = TareaMemoryDataSource(),
    private val tareaDiskDataSource: TareaDiskDataSource = TareaDiskDataSource()
) {
    private val _tareasStream = MutableStateFlow(listOf<Tarea>())

    fun getTareasEnDisco(fileInputStream:FileInputStream) {
        val tareas = tareaDiskDataSource.obtener(fileInputStream)
        insertar(*tareas.toTypedArray())
    }

    fun guardarTareasEnDisco(fileOutputStream:FileOutputStream) {
        tareaDiskDataSource.guardar(fileOutputStream, tareaMemoryDataSource.obtenerTodas())
    }

    fun getTareasStream():StateFlow<List<Tarea>> {
        _tareasStream.update {
            ArrayList(tareaMemoryDataSource.obtenerTodas())
        }
        return _tareasStream.asStateFlow()
    }

    fun insertar(vararg tareas:Tarea) {
        tareaMemoryDataSource.insertar(*tareas) // spread operator (*)
        getTareasStream()
    }

    fun eliminar(tarea:Tarea) {
        tareaMemoryDataSource.eliminar(tarea)
        getTareasStream()
    }
}