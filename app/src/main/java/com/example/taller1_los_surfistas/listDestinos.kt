package com.example.taller1_los_surfistas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class listDestinos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_destinos)
        // Recibir categoria seleccionada
        val categoriaRecibida = intent.getStringExtra("categoria") ?: ""
        // Arreglo con las coincidencias de la categoria seleccionada
        val destinosFiltrados = filtrarDestinos(categoriaRecibida)
        configurarListView(destinosFiltrados)
    }

    fun filtrarDestinos(categoriaRecibida: String): MutableList<String> {
        val destinosFiltrados = mutableListOf<String>()
        //Buscar los destinos que coincidan con la categoria
        for (destino in MainActivity.destinos) {
            if (destino != null) {
                val nombre = destino.split(", ").find { it.startsWith("Nombre:") }?.replace("Nombre: ", "")
                val categoria = destino.split(", ").find { it.startsWith("Categoría:") }?.replace("Categoría: ", "")
                if ((categoriaRecibida == "Todos" || categoria == categoriaRecibida) && nombre != null) {
                    destinosFiltrados.add(nombre)
                } else {
                    Log.e("ProblemaCategoria", "No se encuentra la categoria")
                }
            }
        }
        return destinosFiltrados
    }

    fun configurarListView(destinosFiltrados: List<String>){
        //Definicion de la lista de interfaz
        val listDestinos = findViewById<ListView>(R.id.listViewDestinos)
        val adapter = ArrayAdapter(baseContext,
            android.R.layout.simple_list_item_1,
            destinosFiltrados)
        listDestinos.adapter = adapter
        listDestinos.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = destinosFiltrados[position]
            Log.d("listDestinos", "Elemento seleccionado: $selectedItem")

            // Crear un Intent para iniciar la nueva actividad
            val intent = Intent(this, conFiltro::class.java)
            intent.putExtra("destinoNombre", selectedItem)
            Log.d("listDestinos", "Iniciando actividad con destinoNombre: $selectedItem")
            startActivity(intent)
        }
    }
}