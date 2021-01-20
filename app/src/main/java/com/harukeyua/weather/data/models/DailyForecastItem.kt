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

package com.harukeyua.weather.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dailyForecast")
data class DailyForecastItem(
    val cityId: Long,
    val timezoneOffset: Int,
    val dt: Long,
    val pressure: Int,
    val humidity: Int,
    val wind_speed: Float,
    val wind_deg: Int,
    @Embedded val temp: DailyTemp,
    @Embedded val weather: Weather,
    @PrimaryKey(autoGenerate = true) var key: Int? = null
) {
    companion object {
        fun from(cityId: Long, offset: Int, weather: DailyWeather): DailyForecastItem =
            DailyForecastItem(
                cityId = cityId,
                timezoneOffset = offset,
                dt = weather.dt,
                pressure = weather.pressure,
                humidity = weather.humidity,
                wind_speed = weather.wind_speed,
                wind_deg = weather.wind_deg,
                temp = weather.temp,
                weather = weather.weather.first()
            )
    }
}
