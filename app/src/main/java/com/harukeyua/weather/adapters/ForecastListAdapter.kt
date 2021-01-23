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

package com.harukeyua.weather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.harukeyua.weather.R
import com.harukeyua.weather.data.models.DailyForecastItem
import com.harukeyua.weather.data.models.Weather
import com.harukeyua.weather.databinding.WeekForecastItemBinding
import java.util.*
import kotlin.math.roundToInt

class ForecastListAdapter :
    ListAdapter<DailyForecastItem, ForecastListAdapter.ForecastViewHolder>(WeekForecastCDiffCallback()) {

    class ForecastViewHolder(private val binding: WeekForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DailyForecastItem) {
            with(binding) {
                weatherIcon.setImageResource(Weather.getWeatherIcon(item.weather.id))
                temperatureText.text =
                    root.context.getString(R.string.temp_celsius, item.temp.day.roundToInt())
                weekDayText.text = root.context.getString(R.string.week_day, Date(item.dt * 1000L))
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(
            WeekForecastItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class WeekForecastCDiffCallback : DiffUtil.ItemCallback<DailyForecastItem>() {

    override fun areItemsTheSame(
        oldItem: DailyForecastItem,
        newItem: DailyForecastItem
    ): Boolean {
        return oldItem.cityId == oldItem.cityId
    }

    override fun areContentsTheSame(
        oldItem: DailyForecastItem,
        newItem: DailyForecastItem
    ): Boolean {
        return oldItem.dt == newItem.dt && oldItem.cityId == oldItem.cityId
    }
}