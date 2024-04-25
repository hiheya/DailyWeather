package work.icu007.dailyweather.logic.model


/*
 * Author: Charlie_Liam
 * Time: 2024/3/26-17:03
 * E-mail: rookie_l@icu007.work
 */

data class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily, val hourly: HourlyResponse.Hourly)