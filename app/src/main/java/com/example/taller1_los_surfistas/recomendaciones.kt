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

        configurarTextView(
            recommendedDestination,
            recommendedCountry,
            recommendedCategory,
            recommendedActivity,
            recommendedPrice
        )
    }

    fun configurarTextView(destination: String?,country: String?,category: String?,activity: String?,price: String?) {
        val textViewNombreDestino = findViewById<TextView>(R.id.textViewNombreDestino)
        val textViewRecommendation = findViewById<TextView>(R.id.textViewRecommendation)
        if (destination != null && activity != null) {
            textViewNombreDestino.text = "$destination"
            textViewRecommendation.text = "$country\n" +
                    "$category\n" +
                    "$activity\n" +
                    "USD $price"
        } else {
        }
    }
}