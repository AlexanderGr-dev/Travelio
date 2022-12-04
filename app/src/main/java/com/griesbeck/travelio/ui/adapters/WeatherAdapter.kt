package com.griesbeck.travelio.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.griesbeck.travelio.databinding.CardSightDetailBinding
import com.griesbeck.travelio.databinding.CardWeatherBinding
import com.griesbeck.travelio.models.weather.Daily

class WeatherAdapter(private val weather: Daily) :
RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {


    class ViewHolder(private val binding: CardWeatherBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(weather: Daily, pos: Int){
            binding.tvDate.text = weather.time[pos]
            binding.tvWeatherContent.text = getWeatherFromCode(weather.weathercode[pos])
            binding.tvTempMaxContent.text = weather.temperature_2m_max[pos].toString()
            binding.tvTempMinContent.text = weather.temperature_2m_min[pos].toString()
        }

        private fun getWeatherFromCode(code: Int): String{
            when(code){
                0 -> return "Clear Sky"
                1,2,3 -> return "Partly cloudy"
                45,48 -> return "Foggy"
                51,53,55 -> return "Drizzle"
                56, 57 -> return "Freezing Drizzle"
                61 -> return "Slight rain"
                62 -> return "Moderate rain"
                63 -> return "Heavy rain"
                66,67 -> return "Freezing rain"
                71,73 -> return "Slight snowfall"
                75 -> return "Strong snowfall"
                77 -> return "Snow grains"
                80,81 -> return "Slight rain showers"
                82 -> return "Strong rain showers"
                85 -> return "Slight snow showers"
                86 -> return "Heavy snow showers"
                95 -> return "Thunderstorm"
                else -> return "Unclear condition"
            }
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = CardWeatherBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(weather,position)
    }

    override fun getItemCount(): Int {
        return weather.time.size
    }
}