package work.icu007.dailyweather

import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import android.os.Bundle

/*
每日天气: https://api.caiyunapp.com/v2.6/LkDzF6S9JAyl7hih/116.407387,39.904179/daily.json

天气现象: https://docs.caiyunapp.com/docs/tables/skycon/
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}