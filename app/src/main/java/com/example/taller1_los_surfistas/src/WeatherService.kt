package com.example.taller1_los_surfistas.src

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import com.google.gson.Gson
import java.io.IOException
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class WeatherService(private val apiKey: String) {

        fun requestWeatherData(cityName: String, callback: (Boolean, Int, WeatherResponse?) -> Unit) {
            // Codificar el nombre de la ciudad para evitar problemas con caracteres especiales
            val cityNameEncoded = URLEncoder.encode(cityName, "UTF-8")
            val url = "https://api.weatherapi.com/v1/current.json?key=$apiKey&q=$cityNameEncoded&lan=es"

            // Construir la solicitud
            val request = Request.Builder().url(url).build()

            // Configurar OkHttpClient con tiempos de espera personalizados
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            // Hacer la solicitud
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("Weather", "Request failed: ${e.message}")
                    callback(true, 0, null)
                }

                override fun onResponse(call: Call, response: Response) {
                    response.body?.let {
                        val json = it.string()
                        val weatherResponse = parseWeatherData(json)
                        callback(false, response.code, weatherResponse)
                    } ?: callback(true, response.code, null)
                }
            })
        }

        private fun parseWeatherData(json: String): WeatherResponse? {
            return Gson().fromJson(json, WeatherResponse::class.java)
        }
    }

