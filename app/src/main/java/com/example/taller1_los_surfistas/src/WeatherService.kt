package com.example.taller1_los_surfistas.src

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import com.google.gson.Gson
import java.io.IOException

class WeatherService(private val apiKey: String) {

    fun requestWeatherData(cityName: String, callback: (Boolean, Int, WeatherResponse?) -> Unit) {
        // Construir la URL para la solicitud
        val url = "http://api.weatherapi.com/v1/current.json?key=$apiKey&q=$cityName"

        val request = Request.Builder().url(url).build()

        // Usar OkHttpClient para hacer la solicitud
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
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
