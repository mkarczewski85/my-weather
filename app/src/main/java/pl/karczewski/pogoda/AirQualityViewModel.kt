package pl.karczewski.pogoda

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.karczewski.pogoda.data.Constants
import pl.karczewski.pogoda.data.NetworkResponse
import pl.karczewski.pogoda.data.RetrofitInstance
import pl.karczewski.pogoda.data.model.aqi.AirPollutionDataModel

class AirQualityViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi

    private val _airQualityResult = MutableLiveData<NetworkResponse<AirPollutionDataModel>>()
    val airQualityResult: LiveData<NetworkResponse<AirPollutionDataModel>> = _airQualityResult

    fun getAirQualityData(city: String) {
        _airQualityResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            fetchAirQualityData(city)
        }
    }

    private suspend fun fetchAirQualityData(city: String) {
        try {
            val geoCoordinates = fetchGeoCoordinates(city) ?: return

            val response = weatherApi.getAirQualityData(
                latitude = geoCoordinates.lat.toString(),
                longitude = geoCoordinates.lon.toString(),
                apiKey = Constants.API_KEY,
                units = Constants.API_UNITS
            )

            if (response.isSuccessful) {
                response.body()?.let {
                    _airQualityResult.value = NetworkResponse.Success(it)
                } ?: run {
                    _airQualityResult.value = NetworkResponse.Error("Nie znaleziono.")
                }
            } else {
                _airQualityResult.value = NetworkResponse.Error("Ups! Coś poszło nie tak.")
            }
        } catch (e: Exception) {
            Log.e("AirQualityViewModel", "Exception: ${e.message}", e)
            _airQualityResult.value = NetworkResponse.Error("Ups! Coś poszło nie tak. ${e.message}")
        }
    }

    private suspend fun fetchGeoCoordinates(city: String) = try {
        weatherApi.getGeocodingData(
            apiKey = Constants.API_KEY,
            city = city
        ).body()?.first()
    } catch (e: Exception) {
        Log.e("AirQualityViewModel", "Failed to fetch geo coordinates: ${e.message}", e)
        null
    }
}
