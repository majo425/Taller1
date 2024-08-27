package com.example.taller1_los_surfistas

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    companion object {
        var destinos: Array<String?> = arrayOf()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Cargar los datos de los destinos al arreglo
        val json = JSONObject(loadJSONFromAsset())
        val destinosJson = json.getJSONArray("destinos")
        destinos = arrayOfNulls<String>(destinosJson.length())
        val categoriasSet = mutableSetOf<String>() //para guardar los valores de categoria


        for (i in 0 until destinosJson.length()) {
            val jsonObject = destinosJson.getJSONObject(i)
            val nombre = jsonObject.getString("nombre")
            val pais = jsonObject.getString("pais")
            val categoria = jsonObject.getString("categoria")
            val plan = jsonObject.getString("plan")
            val precio = jsonObject.getInt("precio")
            destinos[i] = "Nombre: $nombre, País: $pais, Categoría: $categoria, Plan: $plan, Precio: $precio"
            categoriasSet.add(categoria)
        }
        val categoriasList = categoriasSet.toList()
        //Definicion de los elementos de la interfaz
        val categorias = findViewById<Spinner>(R.id.spinnerCategoria)
        val btnExplorar = findViewById<Button>(R.id.buttonExplorar)

        val listaCategorias = mutableListOf("Todos") //lo que hace mutable es guardar los valores unicos :0 como un .unique()
        listaCategorias.addAll(categoriasList)

        // Configurar el adaptador para el Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaCategorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorias.adapter = adapter

        //Enviar la peticion al oprimir el boton
        btnExplorar.setOnClickListener {
            //Obtener valor de la categoria seleccionado
            val categoriaSeleccionada = categorias.selectedItem.toString()
            //Log.d("MainActivity", "Categoría seleccionada: $categoriaSeleccionada")

            //Construir la peticion de envio
            val peticion = Intent(this,listDestinos::class.java)
            peticion.putExtra("categoria", categoriaSeleccionada)

            //Enviar la peticion
            startActivity(peticion)
        }
    }

    fun loadJSONFromAsset(): String? {
        var json: String? = null
        try {
            val isStream: InputStream = assets.open("destinos.json")
            val size: Int = isStream.available()
            val buffer = ByteArray(size)
            isStream.read(buffer)
            isStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}