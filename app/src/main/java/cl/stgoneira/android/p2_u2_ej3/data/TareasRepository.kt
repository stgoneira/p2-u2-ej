package cl.stgoneira.android.p2_u2_ej3.data

import cl.stgoneira.android.p2_u2_ej3.data.modelo.Tarea
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TareasRepository(
    private val tareaDataSource:TareaMemoryDataSource = TareaMemoryDataSource()
) {

    private val _tareasStream = MutableStateFlow(listOf<Tarea>())

    fun getTareasStream():StateFlow<List<Tarea>> {
        _tareasStream.update {
            ArrayList(tareaDataSource.obtenerTodas())
        }
        return _tareasStream.asStateFlow()
    }

    fun insertar(vararg tareas:Tarea) {
        tareaDataSource.insertar(*tareas) // spread operator (*)
        getTareasStream()
    }

    fun eliminar(tarea:Tarea) {
        tareaDataSource.eliminar(tarea)
        getTareasStream()
    }
}