package com.example.newweatherapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newweatherapp.MainViewModel
import com.example.newweatherapp.R
import com.example.newweatherapp.adapters.WeatherAdapter
import com.example.newweatherapp.adapters.WeatherModel
import com.example.newweatherapp.databinding.FragmentHoursBinding

class HoursFragment : Fragment() {
    private val adapter = WeatherAdapter()
    private lateinit var binding: FragmentHoursBinding
    private val model : MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHoursBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecView()
    }

    private fun initRecView() {
        binding.apply {
            recView.adapter = adapter
            recView.layoutManager = LinearLayoutManager(activity)
            recView.setHasFixedSize(true)
            val list = listOf(
                WeatherModel("Moscow", "13", "cloudi", "12", "35", "2", "", ""),
                WeatherModel("Spb", "13", "cloudi", "12", "35", "2", "", ""),
                WeatherModel("Novgoto", "13", "cloudi", "12", "35", "2", "", ""),
                WeatherModel("Rostov", "13", "cloudi", "12", "35", "2", "", ""),
                WeatherModel("Norilsk", "13", "cloudi", "12", "35", "2", "", ""),
                WeatherModel("Ryazan", "13", "cloudi", "12", "35", "2", "", ""),
                WeatherModel("Kazan", "13", "cloudi", "12", "35", "2", "", ""),
                WeatherModel("Moscow", "13", "cloudi", "12", "35", "2", "", ""),
                WeatherModel("Spb", "13", "cloudi", "12", "35", "2", "", ""),
                WeatherModel("Novgoto", "13", "cloudi", "12", "35", "2", "", ""),
                WeatherModel("Rostov", "13", "cloudi", "12", "35", "2", "", ""),
                WeatherModel("Norilsk", "13", "cloudi", "12", "35", "2", "", ""),
                WeatherModel("Ryazan", "13", "cloudi", "12", "35", "2", "", ""),
                WeatherModel("Kazan", "13", "cloudi", "12", "35", "2", "", ""),
            )
            adapter.submitList(list)  // Загружаем данные в адаптере
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}