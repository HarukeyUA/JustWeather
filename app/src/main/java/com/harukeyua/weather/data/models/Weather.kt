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

import com.harukeyua.weather.R
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Weather(
    val id: Int,
    val description: String,
    val icon: String
) {
    companion object {
        fun getWeatherIcon(condition: Int): Int {
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
}
