package work.icu007.dailyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context


/*
 * Author: Charlie_Liam
 * Time: 2024/3/19-15:35
 * E-mail: rookie_l@icu007.work
 */

class DailyWeatherApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}