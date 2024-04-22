package work.icu007.dailyweather.ui.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import work.icu007.dailyweather.R
import work.icu007.dailyweather.databinding.ActivityWeatherBinding
import work.icu007.dailyweather.logic.model.Weather
import work.icu007.dailyweather.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }
    private lateinit var aWeatherBinding: ActivityWeatherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        aWeatherBinding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(aWeatherBinding.root)

        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }

        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }

        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }

        Log.d(
            "WeatherActivity",
            "onCreate: viewModelLNG: ${viewModel.locationLng}, viewModelLAT: ${viewModel.locationLat}, viewModelPlaceName: ${viewModel.placeName}"
        )

        viewModel.weatherLiveData.observe(this) { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取到天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        }
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
    }

    private fun showWeatherInfo(weather: Weather) {
        aWeatherBinding.nowLayout.placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily

        // inflate now
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        aWeatherBinding.nowLayout.currentTemp.text = currentTempText
        aWeatherBinding.nowLayout.currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        aWeatherBinding.nowLayout.currentAQI.text = currentPM25Text

        aWeatherBinding.nowLayout.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        // inflate forecast
        aWeatherBinding.forecastLayout.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(
                R.layout.forecast_item,
                aWeatherBinding.forecastLayout.forecastLayout,
                false
            )
            val dateInfo = view.findViewById<TextView>(R.id.dateInfo)
            val skyIcon = view.findViewById<ImageView>(R.id.skyIcon)
            val skyInfo = view.findViewById<TextView>(R.id.skyInfo)
            val temperatureInfo = view.findViewById<TextView>(R.id.temperatureInfo)

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            Log.d(
                "WeatherActivity",
                "showWeatherInfo: skycon.value: ${skycon.value}, skycon.data: ${skycon.date}"
            )
            Log.d(
                "WeatherActivity",
                "showWeatherInfo: sunrise time: ${daily.astro[i].date}, ${daily.astro[i].sunrise.time}, ${daily.astro[i].sunset.time}"
            )
            dateInfo.text = simpleDateFormat.format(skycon.date)

            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            aWeatherBinding.forecastLayout.forecastLayout.addView(view)
        }

        // inflate life_index.xml data
        val lifeIndex = daily.lifeIndex
        aWeatherBinding.lifeIndexLayout.coldRiskText.text = lifeIndex.coldRisk[0].desc
        aWeatherBinding.lifeIndexLayout.dressingText.text = lifeIndex.dressing[0].desc
        aWeatherBinding.lifeIndexLayout.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        aWeatherBinding.lifeIndexLayout.carWashingText.text = lifeIndex.carWashing[0].desc
        aWeatherBinding.weatherLayout.visibility = View.VISIBLE
    }
}