package work.icu007.dailyweather.ui.place;

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import work.icu007.dailyweather.R
import work.icu007.dailyweather.logic.model.HourlyResponse.Hourly
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

/*
 * Author: Charlie Liao
 * Time: 2024/4/23-17:49
 * E-mail: charlie.liao@icu007.work
 */

class HourlyAdapter(private val hourlyList: List<Hourly>) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTime: TextView = view.findViewById(R.id.hourlyItemDateTimeText)
        val temperature: TextView = view.findViewById(R.id.hourlyItemTemperatureText)
        val precipitation: TextView = view.findViewById(R.id.hourlyItemPrecipitationText)
        val skyIcon: ImageView = view.findViewById(R.id.hourlyItemSkyIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.hourly_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = hourlyList[0].precipitation.size

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hourly = hourlyList[0]
        /*for (i in 1..20) {
            Log.d("HourlyAdapter", "onBindViewHolder: datetime: ${hourly.precipitation[i].datetime}, temperature: ${hourly.temperature[i].value}, precipitation: ${hourly.precipitation[i].probability}")
        }*/
        val dateTimeStr = hourly.precipitation[position].datetime
        val odt = OffsetDateTime.parse(dateTimeStr)
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val timeStr = odt.format(formatter)
        holder.dateTime.text =  timeStr
        holder.temperature.text = "${hourly.temperature[position].value.toInt()}℃"
        if (hourly.precipitation[position].probability.toInt() > 30) {
            holder.precipitation.text = "${hourly.precipitation[position].probability.toInt()}%"
        }
//        Log.d("HourlyAdapter", "onBindViewHolder: datetime: ${hourly.precipitation[position].datetime}, temperature: ${hourly.temperature[position].value}, precipitation: ${hourly.precipitation[position].probability}")
        val skycon = hourly.skycon[position].value
        when (skycon) {
            "CLEAR_DAY" -> holder.skyIcon.setImageResource(R.drawable.ic_clear_day)
            "CLEAR_NIGHT" -> holder.skyIcon.setImageResource(R.drawable.ic_clear_night)
            "PARTLY_CLOUDY_DAY" -> holder.skyIcon.setImageResource(R.drawable.ic_partly_cloud_day)
            "PARTLY_CLOUDY_NIGHT" -> holder.skyIcon.setImageResource(R.drawable.ic_partly_cloud_night)
            "CLOUDY" -> holder.skyIcon.setImageResource(R.drawable.ic_cloudy)
            "WIND" -> holder.skyIcon.setImageResource(R.drawable.ic_cloudy)
            "LIGHT_HAZE" -> holder.skyIcon.setImageResource(R.drawable.ic_light_haze)
            "MODERATE_HAZE" -> holder.skyIcon.setImageResource(R.drawable.ic_moderate_haze)
            "HEAVY_HAZE" -> holder.skyIcon.setImageResource(R.drawable.ic_heavy_haze)
            "LIGHT_RAIN" -> holder.skyIcon.setImageResource(R.drawable.ic_light_rain)
            "MODERATE_RAIN" -> holder.skyIcon.setImageResource(R.drawable.ic_moderate_rain)
            "HEAVY_RAIN" -> holder.skyIcon.setImageResource(R.drawable.ic_heavy_rain)
            "STORM_RAIN" -> holder.skyIcon.setImageResource(R.drawable.ic_storm_rain)
            "FOG" -> holder.skyIcon.setImageResource(R.drawable.ic_fog)
            "LIGHT_SNOW" -> holder.skyIcon.setImageResource(R.drawable.ic_light_snow)
            "MODERATE_SNOW" -> holder.skyIcon.setImageResource(R.drawable.ic_moderate_snow)
            "HEAVY_SNOW" -> holder.skyIcon.setImageResource(R.drawable.ic_heavy_snow)
            "STORM_SNOW" -> holder.skyIcon.setImageResource(R.drawable.ic_heavy_snow)
            "HAIL" -> holder.skyIcon.setImageResource(R.drawable.ic_hail)
            "SLEET" -> holder.skyIcon.setImageResource(R.drawable.ic_sleet)
            "UNKNOWN" -> holder.skyIcon.setImageResource(R.drawable.ic_cloudy)
        }
    }
}
