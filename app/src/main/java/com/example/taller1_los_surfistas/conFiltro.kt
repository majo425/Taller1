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

        // Inicializar el servicio de clima
        service = WeatherService("3ea3e5d9c3d14504b9d164305242708")
        // Obtener el nombre del destino desde el Intent
        val destinoNombre = intent.getStringExtra("destinoNombre")

        destinoNombre?.let{
            cargarDatosDestino(it)
        }
        configurarButtonFavoritos()
    }

    fun cargarDatosDestino(destinoNombre: String){
        val jsonString = loadJSONFromAsset()
        if (jsonString != null) {
            val jsonObject = JSONObject(jsonString)
            val destinosArray = jsonObject.getJSONArray("destinos")

            // Buscar el destino por nombre
            for (i in 0 until destinosArray.length()) {
                val destino = destinosArray.getJSONObject(i)
                val nombre = destino.getString("nombre")

                if (nombre == destinoNombre) {
                    currentDestino = destino
                    confijurarTextViewDestino(destino)
                    val ciudad = destino.getString("pais")
                    getWeatherInfo(ciudad)
                    break
                }
            }
        }
    }

    fun confijurarTextViewDestino(destino: JSONObject){
        val textViewDestino = findViewById<TextView>(R.id.textViewDestino)
        val textViewInformacion = findViewById<TextView>(R.id.textViewInformacion)

        textViewDestino.text = destino.getString("nombre")
        textViewInformacion.text = "${destino.getString("categoria")}\n" +
                "${destino.getString("plan")}\n" +
                "${destino.getString("pais")}\n" +
                "USD ${destino.getString("precio")}"
    }

    private fun getWeatherInfo(cityName: String) {
        service.requestWeatherData(cityName) { isNetworkError, statusCode, weatherResponse ->
            runOnUiThread {
                if (!isNetworkError) {
                    if (statusCode == 200) {
                        showWeatherInfo(weatherResponse)
                    } else {
                        Log.d("Weather", "Service error - Status Code: $statusCode")
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

    fun configurarButtonFavoritos(){
        findViewById<Button>(R.id.buttonAddToFavorites).setOnClickListener {
            currentDestino?.let {
                FavoriteDestinations.favoritesList.add(it)
                Toast.makeText(this, "Agregado a Favoritos", Toast.LENGTH_SHORT).show()
                Log.d("Favoritos por el momento:", FavoriteDestinations.favoritesList.toString())
            }
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
