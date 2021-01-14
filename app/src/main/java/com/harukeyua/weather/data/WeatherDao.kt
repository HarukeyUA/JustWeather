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
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harukeyua.weather.data.models.CurrentWeatherResponse

@Dao
interface WeatherDao {
    @Query("SELECT * FROM currentWeather WHERE city = :city")
    fun getCurrentWeather(city: String): LiveData<CurrentWeatherResponse?>

    @Query("SELECT * FROM currentWeather WHERE id = (SELECT id FROM savedCities WHERE selected)")
    fun getCurrentWeather(): LiveData<CurrentWeatherResponse?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(weather: CurrentWeatherResponse)
}