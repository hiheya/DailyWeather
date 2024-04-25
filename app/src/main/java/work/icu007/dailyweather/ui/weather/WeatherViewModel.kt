package work.icu007.dailyweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import work.icu007.dailyweather.logic.Repository
import work.icu007.dailyweather.logic.model.HourlyResponse.Hourly
import work.icu007.dailyweather.logic.model.Location


/*
 * Author: Charlie_Liam
 * Time: 2024/3/26-19:16
 * E-mail: rookie_l@icu007.work
 */

class WeatherViewModel : ViewModel() {
    private val locationLiveData = MutableLiveData<Location>()

    var locationLng = ""
    var locationLat = ""
    var placeName = ""
    var hourly = Hourly("", "", emptyList(), emptyList(), emptyList())

    val weatherLiveData = locationLiveData.switchMap { _ ->
        Repository.refreshWeather(locationLng, locationLat)
    }

    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lng, lat)
    }
}