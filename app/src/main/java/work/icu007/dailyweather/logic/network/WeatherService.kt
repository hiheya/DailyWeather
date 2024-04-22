package work.icu007.dailyweather.logic.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import work.icu007.dailyweather.DailyWeatherApplication
import work.icu007.dailyweather.logic.model.DailyResponse
import work.icu007.dailyweather.logic.model.RealtimeResponse


/*
 * Author: Charlie_Liam
 * Time: 2024/3/26-17:04
 * E-mail: rookie_l@icu007.work
 */

interface WeatherService {
    @GET("v2.6/${DailyWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<RealtimeResponse>

    @GET("v2.6/${DailyWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<DailyResponse>
}