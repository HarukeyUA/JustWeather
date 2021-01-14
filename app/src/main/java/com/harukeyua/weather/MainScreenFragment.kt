/*
 * Copyright 2021 Nazar Rusnak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.harukeyua.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.harukeyua.weather.databinding.FragmentMainScreenBinding
import com.harukeyua.weather.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainScreenFragment : Fragment() {

    private val weatherViewModel: WeatherViewModel by viewModels()

    private lateinit var binding: FragmentMainScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(layoutInflater)
        subscribe()
        setCallbacks()

        weatherViewModel.updateWeather()

        return binding.root
    }

    private fun subscribe() {
        weatherViewModel.selectedCity.observe(viewLifecycleOwner) { item ->
            binding.selectedCityLabel.text = item?.name ?: "Unknown"
        }

        weatherViewModel.currentWeather.observe(viewLifecycleOwner) { weather ->
            weather?.let {
                Log.d("MainScreenFragment", weather.condition.temperature.toString())
                with(binding) {
                    currentTempText.text =
                        getString(R.string.temp_celsius, weather.condition.temperature)
                    currentHumidityText.text =
                        getString(R.string.percent, weather.condition.humidity)
                    currentWindText.text =
                        getString(R.string.meters_per_second, weather.wind.speed)
                    currentWeatherIcon.setImageResource(getWeatherIcon(weather.weather[0].id))
                    currentWeatherDesc.text =
                        weather.weather[0].description.capitalize(Locale.getDefault())
                }
            }
        }
    }

    private fun setCallbacks() {
        binding.selectedCityLabel.setOnClickListener {
            val directions =
                MainScreenFragmentDirections.actionMainScreenFragmentToCitySelectionFragment()
            findNavController().navigate(directions)
        }
    }

    private fun getWeatherIcon(condition: Int): Int {
        return when (condition) {
            in 200..232 -> R.drawable.ic_thunderstorms
            in 300..511 -> R.drawable.ic_rainy
            in 520..531 -> R.drawable.ic_showers
            in 600..622 -> R.drawable.ic_snowy
            in 701..781 -> R.drawable.ic_foggy
            800 -> R.drawable.ic_sun
            801 -> R.drawable.ic_sun_cloudy
            in 802..804 -> R.drawable.ic_cloudy
            else -> R.drawable.ic_sun
        }
    }
}