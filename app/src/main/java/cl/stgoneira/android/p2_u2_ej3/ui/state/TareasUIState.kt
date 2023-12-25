package cl.stgoneira.android.p2_u2_ej3.ui.state

import cl.stgoneira.android.p2_u2_ej3.data.modelo.Tarea

data class TareasUIState (
    val mensaje:String = "",
    val tareas:List<Tarea> = listOf<Tarea>()
)