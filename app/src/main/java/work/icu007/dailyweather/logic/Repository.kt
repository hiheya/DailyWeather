package work.icu007.dailyweather.logic

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import work.icu007.dailyweather.logic.model.Place
import work.icu007.dailyweather.logic.network.DailyWeatherNetwork
import java.lang.RuntimeException


/*
 * Author: Charlie_Liam
 * Time: 2024/3/19-17:00
 * E-mail: rookie_l@icu007.work
 */

object Repository {
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = DailyWeatherNetwork.searchPlace(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is" +
                        "${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }
}
