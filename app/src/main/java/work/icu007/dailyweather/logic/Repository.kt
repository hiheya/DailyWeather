package work.icu007.dailyweather.logic

import android.util.Log
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import work.icu007.dailyweather.logic.dao.PlaceDao
import work.icu007.dailyweather.logic.model.Place
import work.icu007.dailyweather.logic.model.Weather
import work.icu007.dailyweather.logic.network.DailyWeatherNetwork
import kotlin.coroutines.CoroutineContext


/*
 * Author: Charlie_Liam
 * Time: 2024/3/19-17:00
 * E-mail: rookie_l@icu007.work
 */

object Repository {
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = DailyWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(
                RuntimeException(
                    "response status is" +
                            "${placeResponse.status}"
                )
            )
        }

    }

    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                DailyWeatherNetwork.getRealtimeWeather(lng, lat)
            }

            val deferredDaily = async {
                DailyWeatherNetwork.getDailyWeather(lng, lat)
            }

            val deferHourly = async {
                DailyWeatherNetwork.getHourlyWeather(lng, lat)
            }

            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            val hourlyResponse = deferHourly.await()

            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok" && hourlyResponse.status == "ok") {
                val weather =
                    Weather(realtimeResponse.result.realtime, dailyResponse.result.daily, hourlyResponse)
                Log.d("Repository", "refreshWeather: weather = $weather")
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }
    fun savePlace(place: Place) = PlaceDao.savePlace(place)
    fun getSavedPlace() = PlaceDao.getSavedPlace()
    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}
