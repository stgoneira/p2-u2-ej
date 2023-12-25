package cl.stgoneira.android.p2_u2_ej3.ui.theme.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.stgoneira.android.p2_u2_ej3.data.TareasRepository
import cl.stgoneira.android.p2_u2_ej3.data.modelo.Tarea
import cl.stgoneira.android.p2_u2_ej3.ui.state.TareasUIState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class TareasViewModel(
    private val tareasRepository: TareasRepository = TareasRepository()
) : ViewModel() {

    private var job: Job? = null

    // deja la versión mutable como privada
    private val _uiState = MutableStateFlow(TareasUIState())
    // y pública la versión de solo lectura
    // así se asegura que sólo el ViewModel pueda modificar
    val uiState:StateFlow<TareasUIState> = _uiState.asStateFlow()

    init {
        obtenerTareas()
    }

    fun obtenerTareas() {
        job?.cancel()
        job = viewModelScope.launch {
            val tareasStream = tareasRepository.getTareasStream()
            tareasStream.collect { tareasActualizadas ->
                Log.v("TareasViewModel", "obtenerTareas() update{}")
                _uiState.update { currentState ->
                    currentState.copy(
                        tareas = tareasActualizadas
                    )
                }
            }
        }
    }

    fun agregarTarea(tarea:String) {
        job = viewModelScope.launch {
            val t = Tarea(UUID.randomUUID().toString(), tarea)
            tareasRepository.insertar(t)
            _uiState.update {
                it.copy(mensaje = "Tarea agregada: ${t.descripcion}")
            }
            obtenerTareas()
        }
    }

    fun eliminarTarea(tarea:Tarea) {
        job = viewModelScope.launch {
            tareasRepository.eliminar(tarea)
            _uiState.update {
                it.copy(mensaje = "Tarea eliminada: ${tarea.descripcion}")
            }
            obtenerTareas()
        }
    }
}