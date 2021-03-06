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
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "currentWeather")
@JsonClass(generateAdapter = true)
data class CurrentWeatherResponse(
    @PrimaryKey val id: Long,
    val weather: List<Weather>,
    @Embedded @Json(name = "main") val condition: WeatherCondition,
    @Json(name = "dt") val time: Long,
    val timezone: Long,
    @Json(name = "name") val city: String,
    @Embedded val wind: Wind,
    @Embedded val coord: Coordinates
)
