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

class Converter {
    @TypeConverter
    fun listToItem(list: List<Weather>): Int = list.first().id

    @TypeConverter
    fun itemToList(conditionId: Int): List<Weather> = listOf(Weather(conditionId))
}