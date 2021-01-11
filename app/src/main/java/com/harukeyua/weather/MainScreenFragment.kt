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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.harukeyua.weather.databinding.FragmentMainScreenBinding
import com.harukeyua.weather.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

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
        return binding.root
    }

    private fun subscribe() {
        weatherViewModel.selectedCity.observe(viewLifecycleOwner) { item ->
            binding.selectedCityLabel.text = item?.name ?: "Unknown"
        }
    }

    private fun setCallbacks() {

        binding.selectedCityLabel.setOnClickListener {
            val directions =
                MainScreenFragmentDirections.actionMainScreenFragmentToCitySelectionFragment()
            findNavController().navigate(directions)
        }

    }
}