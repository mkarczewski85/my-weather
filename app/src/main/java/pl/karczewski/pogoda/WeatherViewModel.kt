package pl.karczewski.pogoda

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.karczewski.pogoda.data.Constants
import pl.karczewski.pogoda.data.NetworkResponse
import pl.karczewski.pogoda.data.RetrofitInstance
import pl.karczewski.pogoda.data.model.weather.CurrentWeatherDataModel

class WeatherViewModel: ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi

    private val _weatherResult = MutableLiveData<NetworkResponse<CurrentWeatherDataModel>>()
    val weatherResult: LiveData<NetworkResponse<CurrentWeatherDataModel>> = _weatherResult

    fun getWeatherData(city: String) {
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = weatherApi.getCurrentWeather(
                        city=city,
                        apiKey = Constants.API_KEY,
                        language = Constants.API_LANG,
                        units = Constants.API_UNITS
                    )
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Error("Nie znaleziono.")
                }
            } catch (e: Exception) {
                _weatherResult.value = NetworkResponse.Error("Ups! Coś poszło nie tak.")
            }
        }
    }
}