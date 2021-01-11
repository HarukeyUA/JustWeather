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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.harukeyua.weather.adapters.CityListAdapter
import com.harukeyua.weather.adapters.MarginItemDecoration
import com.harukeyua.weather.databinding.FragmentCitySelectionBinding
import com.harukeyua.weather.viewmodels.AddCityStatus
import com.harukeyua.weather.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CitySelectionFragment : Fragment() {

    private lateinit var binding: FragmentCitySelectionBinding

    private val viewModel: WeatherViewModel by viewModels()

    private val cityListAdapter: CityListAdapter = CityListAdapter { selectedCity ->
        viewModel.selectCity(selectedCity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCitySelectionBinding.inflate(layoutInflater)
        binding.cityList.adapter = cityListAdapter

        binding.cityList.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_small))
        )

        hideBottomSheet()
        subscribe()
        setCallbacks()
        return binding.root
    }

    private fun subscribe() {
        viewModel.cityList.observe(viewLifecycleOwner) { list ->
            cityListAdapter.submitList(list)
        }

        viewModel.cityList.observe(viewLifecycleOwner) { list ->
            if (list?.isEmpty() == true) {
                BottomSheetBehavior.from(binding.cityAddBottomSheet).state =
                    BottomSheetBehavior.STATE_EXPANDED
            }
        }

        viewModel.saveCityStatus.observe(viewLifecycleOwner) { status ->
            status?.let {
                binding.cityAddProgressIndicator.visibility = View.INVISIBLE
                when (status) {
                    AddCityStatus.CONNECTION_ERROR -> Toast.makeText(
                        requireContext(),
                        R.string.connection_err,
                        Toast.LENGTH_SHORT
                    ).show()
                    AddCityStatus.CITY_NOT_FOUND -> binding.cityAddInputLayout.error =
                        requireContext().getString(R.string.city_not_found)
                    AddCityStatus.OK -> {
                        binding.cityAddInputLayout.error = null
                        hideBottomSheet()
                        binding.cityAddTextInput.text?.clear()
                    }
                }
                viewModel.saveCityStatusHandled()
            }

        }
    }

    private fun hideBottomSheet() {
        BottomSheetBehavior.from(binding.cityAddBottomSheet).state =
            BottomSheetBehavior.STATE_HIDDEN
    }

    private fun openBottomSheet() {
        BottomSheetBehavior.from(binding.cityAddBottomSheet).state =
            BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setCallbacks() {
        binding.cityAddButton.setOnClickListener {
            requestCitySave()
        }

        binding.cityAddTextInput.setOnEditorActionListener { _, _, _ ->
            requestCitySave()
            return@setOnEditorActionListener true
        }

        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add_city -> {
                    openBottomSheet()
                    true
                }
                else -> false
            }
        }

    }

    private fun requestCitySave() {
        binding.cityAddTextInput.text.toString().let {
            if (it.isNotEmpty()) {
                binding.cityAddProgressIndicator.visibility = View.VISIBLE
                viewModel.saveCity(it)
            }
        }
        requireActivity().hideKeyboard()
    }


}