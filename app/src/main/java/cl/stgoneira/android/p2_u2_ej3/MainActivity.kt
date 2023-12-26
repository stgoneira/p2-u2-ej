package cl.stgoneira.android.p2_u2_ej3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.stgoneira.android.p2_u2_ej3.data.modelo.Tarea
import cl.stgoneira.android.p2_u2_ej3.ui.theme.viewmodels.TareasViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    val tareasVm: TareasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("MainActivity::onCreate", "Recuperando tareas en disco")
        try {
            tareasVm.obtenerTareasGuardadasEnDisco(openFileInput(TareasViewModel.FILE_NAME))
        } catch (e:Exception) {
            Log.e("MainActivity::onCreate", "Archivo con tareas no existe!!")
        }

        setContent {
            AppTareas()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.v("MainActivity::onPause", "Guardando a disco")
        tareasVm.guardarTareasEnDisco(openFileOutput(TareasViewModel.FILE_NAME, MODE_PRIVATE))
    }

    override fun onStop() {
        super.onStop()
        Log.v("MainActivity::onStop", "Guardando a disco")

    }
}

@Preview(showSystemUi = true, locale = "es")
@Composable
fun AppTareas(
    tareasVm: TareasViewModel = viewModel()
) {
    val contexto = LocalContext.current
    val textoLogo = contexto.getString(R.string.logo)
    val uiState by tareasVm.uiState.collectAsStateWithLifecycle()
    var mostrarMensaje by rememberSaveable {
        mutableStateOf(false)
    }
    var primeraEjecucion by rememberSaveable {
        mutableStateOf(true)
    }

    LaunchedEffect(uiState.mensaje) {
        if(!primeraEjecucion) {
            mostrarMensaje = true
            delay(2_000)
            mostrarMensaje = false
        }
        primeraEjecucion = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        AnimatedVisibility(
            visible = mostrarMensaje,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = uiState.mensaje,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(10.dp)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.todo_posit),
            contentDescription = textoLogo,
            modifier = Modifier.align(CenterHorizontally)
        )
        TareaFormUI {
            tareasVm.agregarTarea(it)
        }
        Spacer(modifier = Modifier.height(20.dp))
        TareaListaUI(
            tareas = uiState.tareas,
            onDelete = {
                tareasVm.eliminarTarea(it)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaFormUI(
    onClickAgregarTarea:(tarea:String) -> Unit
) {
    val contexto = LocalContext.current
    val textTaskPlaceholder = contexto.getString(R.string.tarea_form_ejemplo)
    val textButtonAddTask = contexto.getString(R.string.tarea_form_agregar)

    val (descripcionTarea, setDescripcionTarea) = rememberSaveable {
        mutableStateOf("")
    }
    Box(
        contentAlignment = Alignment.CenterEnd
        ,modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = descripcionTarea,
            onValueChange = {
                setDescripcionTarea(it)
            },
            placeholder = {Text(textTaskPlaceholder)},
            modifier = Modifier.fillMaxWidth()
        )
        TooltipBox(
            positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
            tooltip = { PlainTooltip {Text(textButtonAddTask)} },
            state = rememberTooltipState()) {
            IconButton(onClick = {
                Log.v("TareaFormUI::IconButton", "Agregar Tarea")
                onClickAgregarTarea(descripcionTarea)
                setDescripcionTarea("")
            }) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = textButtonAddTask,
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        }
    }
}

@Composable
fun TareaListaUI(
    tareas:List<Tarea>,
    onDelete: (t:Tarea) -> Unit
) {
    LazyColumn() {
        items(tareas) {
            TareaListaItemUI(it, onDelete)
        }
    }
}

@Composable
fun TareaListaItemUI(
    tarea: Tarea,
    onDelete: (t:Tarea) -> Unit
) {
    val contexto = LocalContext.current
    val textoEliminarTarea = contexto.getString(R.string.tarea_form_eliminar)

    Column {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = tarea.descripcion,
                fontSize = 20.sp,
                modifier = Modifier
                    .weight(2.0f)
                    .padding(10.dp, 8.dp)
            )
            IconButton(onClick = {
                Log.v("TareaListaItemUI::IconButton", "onClick DELETE")
                onDelete(tarea)
            }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = textoEliminarTarea,
                    tint = Color.Black,
                    modifier = Modifier.size(25.dp)
                )
            }
        }
        HorizontalDivider()
    }
}




