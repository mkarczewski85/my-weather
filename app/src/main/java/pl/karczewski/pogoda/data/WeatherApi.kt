package pl.karczewski.pogoda.data

import pl.karczewski.pogoda.data.model.aqi.AirPollutionDataModel
import pl.karczewski.pogoda.data.model.geo.DirectGeocodingDataModel
import pl.karczewski.pogoda.data.model.geo.GeocodingDataModelItem
import pl.karczewski.pogoda.data.model.weather.CurrentWeatherDataModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("appid") apiKey: String,
        @Query("q") city: String,
        @Query("lang") language: String,
        @Query("units") units: String
    ): Response<CurrentWeatherDataModel>

    @GET("/data/2.5/air_pollution")
    suspend fun getAirQualityData(
        @Query("appid") apiKey: String,
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") units: String
    ): Response<AirPollutionDataModel>

    @GET("/geo/1.0/direct")
    suspend fun getGeocodingData(
        @Query("appid") apiKey: String,
        @Query("q") city: String,
    ): Response<DirectGeocodingDataModel>

}