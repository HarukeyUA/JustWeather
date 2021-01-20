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

package com.harukeyua.weather.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harukeyua.weather.data.WeatherRepo
import com.harukeyua.weather.data.models.City
import kotlinx.coroutines.launch
import retrofit2.HttpException

enum class AddCityStatus {
    CONNECTION_ERROR, CITY_NOT_FOUND, OK
}

class WeatherViewModel @ViewModelInject constructor(private val repo: WeatherRepo) :
    ViewModel() {

    val cityList = repo.savedCityList

    val selectedCity = repo.selectedCity

    val currentWeather = repo.currentWeather

    val dailyForecast = repo.dailyForecast

    private val _saveCityStatus = MutableLiveData<AddCityStatus>()
    val saveCityStatus: LiveData<AddCityStatus>
        get() = _saveCityStatus

    fun saveCity(name: String) {
        viewModelScope.launch {
            try {
                repo.saveCity(name)
                _saveCityStatus.value = AddCityStatus.OK
            } catch (e: HttpException) {
                e.printStackTrace()
                _saveCityStatus.value =
                    if (e.code() == 404) AddCityStatus.CITY_NOT_FOUND else AddCityStatus.CONNECTION_ERROR
            } catch (e: Exception) {
                e.printStackTrace()
                _saveCityStatus.value = AddCityStatus.CONNECTION_ERROR
            }
        }
    }

    fun saveCityStatusHandled() {
        _saveCityStatus.value = null
    }

    fun selectCity(city: City) {
        viewModelScope.launch {
            repo.selectCity(city)
        }
    }

    fun updateWeather() {
        viewModelScope.launch {
            repo.updateCurrentWeather()
            repo.updateWeatherForecast()
        }
    }

}