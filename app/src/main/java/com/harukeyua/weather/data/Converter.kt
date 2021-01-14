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

import androidx.room.TypeConverter
import com.harukeyua.weather.data.models.Weather
import com.squareup.moshi.Moshi

class Converter {
    @TypeConverter
    fun listToItem(list: List<Weather>): String {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(Weather::class.java)
        return adapter.toJson(list.first())
    }

    @TypeConverter
    fun itemToList(condition: String): List<Weather> {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(Weather::class.java)
        val weather = adapter.fromJson(condition)
        return listOf(weather!!)
    }
}