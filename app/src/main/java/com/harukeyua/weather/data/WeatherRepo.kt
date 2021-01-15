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

package com.harukeyua.weather.data

import androidx.lifecycle.LiveData
import com.harukeyua.weather.BuildConfig
import com.harukeyua.weather.api.OWMService
import com.harukeyua.weather.data.models.City
import com.harukeyua.weather.data.models.CurrentWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepo @Inject constructor(
    private val service: OWMService,
    private val weatherDao: WeatherDao,
    private val citiesDao: CitiesDao
) {
    val savedCityList: LiveData<List<City>>
        get() = citiesDao.getSavedCitiesList()

    val selectedCity: LiveData<City>
        get() = citiesDao.getSelectedCity()

    val currentWeather: LiveData<CurrentWeatherResponse?>
        get() = weatherDao.getCurrentWeather()

    suspend fun saveCity(name: String) {
        withContext(Dispatchers.IO) {
            val result = service.currentWeather(name, BuildConfig.WEATHER_KEY)
            val city = City(result.id, result.city, result.coord)
            citiesDao.insertCity(city)
            citiesDao.selectCity(result.id)
        }
    }

    suspend fun selectCity(city: City) {
        withContext(Dispatchers.IO) {
            citiesDao.selectCity(city.id)
        }
    }

    fun getCurrentWeatherForCity(city: String): LiveData<CurrentWeatherResponse?> {
        return weatherDao.getCurrentWeather(city)
    }

    suspend fun updateCurrentWeather(city: String) {
        withContext(Dispatchers.IO) {
            val response = service.currentWeather(city, BuildConfig.WEATHER_KEY)
            weatherDao.insertCurrentWeather(response)
        }
    }

    // Update weather for selected by user city
    suspend fun updateCurrentWeather() {
        withContext(Dispatchers.IO) {
            val selectedCity = citiesDao.getSelectedCityValue()
            selectedCity?.let {
                val response = service.currentWeather(selectedCity.name, BuildConfig.WEATHER_KEY)
                weatherDao.insertCurrentWeather(response)
            }
        }
    }

}