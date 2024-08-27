package com.example.taller1_los_surfistas

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import com.example.taller1_los_surfistas.src.WeatherService
import com.example.taller1_los_surfistas.src.WeatherResponse


object FavoriteDestinations {
    val favoritesList = mutableListOf<JSONObject>()
}

class conFiltro : AppCompatActivity() {
    private var currentDestino: JSONObject? = null
    private lateinit var service: WeatherService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_con_filtro)

        // Inicializar el servicio de clima con tu API Key
        service = WeatherService("3ea3e5d9c3d14504b9d164305242708")

        // Obtener el nombre del destino desde el Intent
        val destinoNombre = intent.getStringExtra("destinoNombre")

        // Leer el archivo JSON desde assets
        val jsonString = loadJSONFromAsset()
        if (jsonString != null) {
            val destinosArray = JSONArray(jsonString)

            // Buscar el destino por nombre
            for (i in 0 until destinosArray.length()) {
                val destino = destinosArray.getJSONObject(i)
                val nombre = destino.getString("Nombre")

                if (nombre == destinoNombre) {
                    currentDestino = destino

                    // Mostrar los detalles del destino en la UI
                    val textViewDestino = findViewById<TextView>(R.id.textViewDestino)
                    textViewDestino.text = "Nombre: ${destino.getString("Nombre")}\n" +
                            "Categoría: ${destino.getString("Categoría")}\n" +
                            "Descripción: ${destino.getString("Descripción")}\n" +
                            "Ubicación: ${destino.getString("Ubicación")}"

                    // Realizar la solicitud de datos meteorológicos para el destino
                    val ciudad = destino.getString("Ubicación") // Suponiendo que esta es la ciudad
                    getWeatherInfo(ciudad)

                    break
                }
            }
        }

        // Configurar el botón para agregar a favoritos
        val buttonAddToFavorites = findViewById<Button>(R.id.buttonAddToFavorites)
        buttonAddToFavorites.setOnClickListener {
            currentDestino?.let {
                FavoriteDestinations.favoritesList.add(it)
                Toast.makeText(this, "Agregado a Favoritos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getWeatherInfo(cityName: String) {
        service.requestWeatherData(cityName) { isNetworkError, statusCode, weatherResponse ->
            runOnUiThread {
                if (!isNetworkError) {
                    if (statusCode == 200) {
                        showWeatherInfo(weatherResponse)
                    } else {
                        Log.d("Weather", "Service error")
                    }
                } else {
                    Log.d("Weather", "Network error")
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showWeatherInfo(weatherResponse: WeatherResponse?) {
        weatherResponse?.let {
            val temp = it.current.temp_c
            val condition = it.current.condition.text

            findViewById<TextView>(R.id.lblCurrent).text = "Temperatura: $temp°C"
            findViewById<TextView>(R.id.lblCondition).text = "Condición: $condition"
        }
    }

    // Función para cargar el archivo JSON desde assets
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
