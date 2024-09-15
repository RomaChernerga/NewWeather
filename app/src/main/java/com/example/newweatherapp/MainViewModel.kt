package com.example.newweatherapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newweatherapp.adapters.WeatherModel

class MainViewModel : ViewModel() {
    val lifeDataCurrent = MutableLiveData<WeatherModel>()
    val lifeDataList = MutableLiveData<List<WeatherModel>>()
    val lifeDaysList = MutableLiveData<List<WeatherModel>>()



}