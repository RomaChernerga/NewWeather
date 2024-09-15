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
import com.example.newweatherapp.databinding.FragmentHoursBinding

class DaysFragment : Fragment() {
    private lateinit var binding : FragmentHoursBinding
    private lateinit var adapter : WeatherAdapter
    private val model : MainViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoursBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recViewInit()
        model.lifeDataList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

    private fun recViewInit() = with(binding) {
        adapter = WeatherAdapter()
        rcView.layoutManager = LinearLayoutManager(activity)
        rcView.adapter = adapter
        rcView.setHasFixedSize(true)

    }

    companion object {
        @JvmStatic
        fun newInstance() = DaysFragment()
    }
}