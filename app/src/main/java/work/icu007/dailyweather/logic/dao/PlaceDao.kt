package work.icu007.dailyweather.logic.dao

import android.content.Context
import com.google.gson.Gson
import work.icu007.dailyweather.DailyWeatherApplication
import work.icu007.dailyweather.logic.model.Place

object PlaceDao {
    fun savePlace(place: Place) {
        sharedPreferences().edit().apply {
            putString("place", Gson().toJson(place))
            apply()
        }
    }

    fun getSavedPlace(): Place {
        val placeJson = sharedPreferences().getString("place", "") ?: ""
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved() = sharedPreferences().contains("place")
    
    private fun sharedPreferences() = DailyWeatherApplication.context.getSharedPreferences("daily_weather", Context.MODE_PRIVATE)
}