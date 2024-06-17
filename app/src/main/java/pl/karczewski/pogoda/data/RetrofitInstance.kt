package pl.karczewski.pogoda.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val weatherApi: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.WEATHER_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

}