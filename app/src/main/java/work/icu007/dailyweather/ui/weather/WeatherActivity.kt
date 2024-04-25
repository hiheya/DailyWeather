package work.icu007.dailyweather.ui.weather

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import work.icu007.dailyweather.R
import work.icu007.dailyweather.databinding.ActivityWeatherBinding
import work.icu007.dailyweather.logic.model.Weather
import work.icu007.dailyweather.logic.model.getSky
import work.icu007.dailyweather.ui.place.HourlyAdapter
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProvider(this)[WeatherViewModel::class.java] }
    private lateinit var adapter: HourlyAdapter // 适配器
    private lateinit var aWeatherBinding: ActivityWeatherBinding
    private lateinit var mRecycleView: RecyclerView // RecyclerView 的引用
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // 设置状态栏透明
        val decorView = window.decorView
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        var systemUiVisibility = decorView.systemUiVisibility
        systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = systemUiVisibility
        window.statusBarColor = Color.TRANSPARENT

        val isNightMode = resources.configuration.uiMode == Configuration.UI_MODE_NIGHT_MASK
        setStatusBarTextColor(window, isNightMode)

        aWeatherBinding = ActivityWeatherBinding.inflate(layoutInflater)
        mRecycleView = aWeatherBinding.hourlyLayout.hourlyRecyclerView
        val layoutManager = LinearLayoutManager(this)
        mRecycleView.layoutManager = layoutManager
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

        // 滑动菜单
        aWeatherBinding.nowLayout.navBtn.setOnClickListener {
            aWeatherBinding.drawerLayout.open()
        }

        aWeatherBinding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
            }

            @SuppressLint("ServiceCast")
            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        })

        viewModel.weatherLiveData.observe(this) { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                viewModel.hourly = weather.hourly
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取到天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            aWeatherBinding.swipeRefresh.isRefreshing = false
        }
        aWeatherBinding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshWeather()
        aWeatherBinding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        aWeatherBinding.swipeRefresh.isRefreshing = true
    }

    private fun showWeatherInfo(weather: Weather) {

        // 毛玻璃效果



        aWeatherBinding.nowLayout.placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
        val hourly = weather.hourly

        // inflate hourly
        mRecycleView = aWeatherBinding.hourlyLayout.hourlyRecyclerView
        val hourlyTitle = hourly.description
        aWeatherBinding.hourlyLayout.descriptionText.text = hourlyTitle
        // 给 RecyclerView 设置适配器
        adapter = HourlyAdapter(viewModel.hourly)
        aWeatherBinding.hourlyLayout.hourlyRecyclerView.adapter = adapter
        // 设置LayoutManager为LinearLayoutManager 水平方向
        aWeatherBinding.hourlyLayout.hourlyRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        // inflate now
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        aWeatherBinding.nowLayout.currentTemp.text = currentTempText
        aWeatherBinding.nowLayout.currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        aWeatherBinding.nowLayout.currentAQI.text = currentPM25Text

//        aWeatherBinding.nowLayout.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        aWeatherBinding.weatherLayout.setBackgroundResource(getSky(realtime.skycon).bg)

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

    fun setStatusBarTextColor(window: Window, light: Boolean) {
        var systemUiVisibility = window.decorView.systemUiVisibility
        systemUiVisibility = if (light) { //白色文字
            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        } else { //黑色文字
            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        window.decorView.systemUiVisibility = systemUiVisibility
    }
}