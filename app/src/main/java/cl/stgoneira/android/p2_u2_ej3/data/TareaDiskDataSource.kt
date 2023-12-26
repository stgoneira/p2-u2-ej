package cl.stgoneira.android.p2_u2_ej3.data

import android.util.Log
import cl.stgoneira.android.p2_u2_ej3.data.modelo.Tarea
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class TareaDiskDataSource() {

    fun obtener(fileInputStream: FileInputStream):List<Tarea> {
        return try {
            fileInputStream.use { fis ->
                ObjectInputStream(fis).use { ois ->
                    ois.readObject() as? List<Tarea> ?: emptyList()
                }
            }
        } catch (fnfex:FileNotFoundException) {
            emptyList<Tarea>()
        } catch (ex:Exception) {
            Log.e("TareaDiskDataSource", "obtener ex:Exception ${ex.toString()}")
            emptyList<Tarea>()
        }
    }

    fun guardar(fileOutputStream: FileOutputStream, tareas:List<Tarea>) {
        fileOutputStream.use { fos ->
            ObjectOutputStream(fos).use { oos ->
                oos.writeObject(tareas)
            }
        }
    }

}