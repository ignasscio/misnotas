package simons.valdez.ignacio.misnotas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.Manifest
import android.content.pm.PackageManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.FileOutputStream

class AgregarNotaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_nota)

        var button_guardar = findViewById(R.id.button_guardar) as Button

        button_guardar.setOnClickListener{
            guardarNota()
        }
    }

    fun guardarNota(){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 235)
        }else{
            guardar()
        }
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Error, permisos denegados", Toast.LENGTH_LONG).show()
        }
    }

    fun guardar(){
        var editText_titulo = findViewById(R.id.editText_titulo) as EditText
        var editText_contenido = findViewById(R.id.editText_contenido) as EditText

        var titulo = editText_titulo.text.toString()
        var contenido = editText_contenido.text.toString()

        if(titulo == "" || contenido == ""){
            Toast.makeText(this, "Error: alguno de los campos está vacío",
                Toast.LENGTH_LONG)
        }else{
            try{
                val archivo = File(ubicacion(), titulo+".txt")
                val fos = FileOutputStream(archivo)
                fos.write(contenido.toByteArray())
                fos.close()
                Toast.makeText(this, "se guardó el archivo en la carpeta pública",
                    Toast.LENGTH_LONG).show()

            }catch(e:Exception){
                Toast.makeText(this, "Error: no se guardó el archivo",
                    Toast.LENGTH_LONG)
            }
        }
        finish()
    }

    fun ubicacion():String{
        val carpeta = File(getExternalFilesDir(null), "notas")
        if(!carpeta.exists()){
            carpeta.mkdir()
        }
        return carpeta.absolutePath
    }
}