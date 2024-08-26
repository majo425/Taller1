package com.icm.taller1.logica

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.icm.taller1.R

class listDestinos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listdestinos)

        //Recibir categoria seleccionada
        var categoriaRecibida = intent.getStringExtra("categoria")

        //Definicion de la lista de interfaz
        val listDestinos = findViewById<ListView>(R.id.listViewDestinos)

        //Arreglo con las coincidencias de la categoria seleccionada
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

        val adapter = ArrayAdapter(baseContext,
            android.R.layout.simple_list_item_1,
            destinosFiltrados)
        listDestinos.adapter = adapter
    }
}