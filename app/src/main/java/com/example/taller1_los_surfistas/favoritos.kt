package com.example.taller1_los_surfistas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class favoritos : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)

        val listViewFavoritos = findViewById<ListView>(R.id.listViewFavoritos)
        // Obtener los destinos favoritos
        val favoritos = FavoriteDestinations.favoritesList
        val nombresFavoritos = favoritos.map { it.getString("nombre") }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nombresFavoritos)
        listViewFavoritos.adapter = adapter

        // Manejar clics en la lista de favoritos
        listViewFavoritos.setOnItemClickListener { parent, view, position, id ->
            val destinoSeleccionado = favoritos[position] // Obtener el destino seleccionado
            val intent = Intent(this, conFiltro::class.java) // Iniciar la actividad con la información del destino
            intent.putExtra("destinoNombre", destinoSeleccionado.getString("nombre"))
            intent.putExtra("ocultarBotonFavoritos", true)  //Para ocultar el botón de "añadir a favoritos" que se encuentra en "conFiltro"
            startActivity(intent)
        }

    }
}