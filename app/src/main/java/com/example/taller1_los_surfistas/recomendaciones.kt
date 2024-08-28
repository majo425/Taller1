package com.example.taller1_los_surfistas

import android.annotation.SuppressLint
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

class recomendaciones : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recomendaciones)

        val recommendedDestination = intent.getStringExtra("recommendedDestination")
        val recommendedCountry = intent.getStringExtra("recommendedCountry")
        val recommendedCategory = intent.getStringExtra("recommendedCategory")
        val recommendedActivity = intent.getStringExtra("recommendedActivity")
        val recommendedPrice = intent.getStringExtra("recommendedPrice")

        val textViewRecommendation = findViewById<TextView>(R.id.textViewRecommendation)
        textViewRecommendation.text = if (recommendedDestination != null && recommendedActivity != null) {
            "Nombre: $recommendedDestination\n" +
                    "Pais: $recommendedCountry\n"
                    "Categoría: $recommendedCategory\n" +
                    "Plan: $recommendedActivity\n" +
                    "Precio: $recommendedPrice"
        } else {
            "NA"
        }
    }
}