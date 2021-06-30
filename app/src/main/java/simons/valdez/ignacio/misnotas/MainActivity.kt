package simons.valdez.ignacio.misnotas

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.*

class MainActivity : AppCompatActivity() {

    var notas:ArrayList<Nota> = ArrayList<Nota>()
    lateinit var adaptador:NotaAdaptador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fab = findViewById(R.id.fab) as FloatingActionButton

        fab.setOnClickListener{
            var intent = Intent(this, AgregarNotaActivity::class.java)
            startActivity(intent)
        }

        leerNotas()

        adaptador = NotaAdaptador(this, notas)

        var listView = findViewById(R.id.listView_notas) as ListView
        listView.adapter = adaptador

    }

    fun leerNotas(){
        notas.clear()
        var carpeta = File(ubicacion())
        if(carpeta.exists()){
            var archivos = carpeta.listFiles()
            if(archivos != null){
                for(archivo in archivos){
                    leerArchivo(archivo)
                }
            }

        }
    }

    fun leerArchivo(archivo: File){
        val fis: FileInputStream = FileInputStream(archivo)
        val dis: DataInputStream = DataInputStream(fis)
        val br: BufferedReader = BufferedReader(InputStreamReader(dis))

        var stringLine:String? = br.readLine()

        var myData:String = ""

        while(stringLine != null){
            myData += stringLine
            stringLine = br.readLine()
        }
        br.close()
        dis.close()
        fis.close()
        var nombre = archivo.name.substring(0, archivo.name.length-4)
        var nota:Nota = Nota(nombre, myData)
        notas.add(nota)

    }

    private fun ubicacion():String{
        val folder = File(getExternalFilesDir(null),"notas")
        if(!folder.exists()){
            folder.mkdir()
        }
        return folder.absolutePath
    }

    class NotaAdaptador:BaseAdapter{

        var notas:ArrayList<Nota> = ArrayList<Nota>()
        var contexto:Context? = null

        constructor(contexto:Context, notas:ArrayList<Nota>){
            this.contexto = contexto
            this.notas = notas
        }

        override fun getCount(): Int {
            return this.notas.size
        }

        override fun getItem(position: Int): Any {
            return this.notas[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var inflator = LayoutInflater.from(contexto)
            var view = inflator.inflate(R.layout.nota_layout, null)
            var nota = notas[position]

            var title:TextView = view.findViewById(R.id.textView_tituloNota) as TextView
            var content:TextView = view.findViewById(R.id.textView_contenidoNota) as TextView

            title.setText(nota.titulo)
            content.setText(nota.contenido)

            var imageView_delete = view.findViewById(R.id.imageView_borrar) as ImageView

            imageView_delete.setOnClickListener{
                eliminar(nota.titulo)
                notas.remove(nota)
                this.notifyDataSetChanged()
            }


            return view
        }

        fun eliminar(titulo:String){
            if(titulo == ""){
                Toast.makeText(contexto, "Error: título vacío", Toast.LENGTH_LONG).show()
            }else{
                try{
                    var archivo = File(ubicacion(), titulo+".txt")
                    archivo.delete()
                    Toast.makeText(contexto, "se eliminó el archivo", Toast.LENGTH_SHORT).show()
                }catch(e:Exception){
                    Toast.makeText(contexto, "Error al eliminar el archivo", Toast.LENGTH_LONG).show()
                }
            }
        }

        fun ubicacion():String{
            val carpeta = File(contexto?.getExternalFilesDir(null), "notas")
            if(!carpeta.exists()){
                carpeta.mkdir()
            }
            return carpeta.absolutePath
        }

    }
}