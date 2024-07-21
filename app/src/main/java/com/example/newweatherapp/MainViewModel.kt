package com.example.newweatherapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val lifeDataCurrent = MutableLiveData<List<String>>()
    val lifeDataList = MutableLiveData<String>()



}