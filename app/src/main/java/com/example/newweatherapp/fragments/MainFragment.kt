package com.example.newweatherapp.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.newweatherapp.MainViewModel
import com.example.newweatherapp.adapters.ViewPagerAdapter
import com.example.newweatherapp.adapters.WeatherModel
import com.example.newweatherapp.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import org.json.JSONObject

const val apiKey = "9f4400beb9144f9abed155933222105"

class MainFragment : Fragment() {
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var binding: FragmentMainBinding
    private val fragmentList = listOf(HoursFragment.newInstance(), DaysFragment.newInstance())
    private val textList = listOf("Hours", "Days")
    private val model: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        updateCurrentCard()
        initViewPager()
        requestWeatherData("Moscow", 3)
    }

    private fun requestWeatherData(city: String, days: Int) {
        val url = "https://api.weatherapi.com/v1/forecast.json?key=" +
                apiKey +
                "&q=" +
                city +
                "&days=$days" +
                "&aqi=no" +
                "&alerts=no"

        val query = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            { result ->
                parseWeatherData(result)
            },
            { error ->
                Log.d("MyLog", error.message.toString())
            }
        )
        query.add(request)
    }

    private fun parseWeatherData(result: String) {
        val mainObject = JSONObject(result)
        val list = parseDays(mainObject)

        parseCurrentData(mainObject, list[0])
    }

    private fun parseDays(mainObject: JSONObject) : List<WeatherModel> {
        val dayList = ArrayList<WeatherModel>()
        val daysArray = mainObject.getJSONObject("forecast").getJSONArray("forecastday")
        val city = mainObject.getJSONObject("location").getString("name")
        for (i in 0 until daysArray.length()) {
            val day = daysArray[i] as JSONObject
            val dayItem = WeatherModel(
                city,
                day.getString("date"),
                day.getJSONObject("day").getJSONObject("condition").getString("text"),
                "",
                day.getJSONObject("day").getString("maxtemp_c"),
                day.getJSONObject("day").getString("mintemp_c"),
                day.getJSONObject("day").getJSONObject("condition").getString("icon"),
                day.getJSONArray("hour").toString()
            )
            dayList.add(dayItem)
        }
        return dayList
    }

    private fun parseCurrentData(mainObject: JSONObject, weatherItem : WeatherModel) {
        val item = WeatherModel(
            mainObject.getJSONObject("location").getString("name"),
            mainObject.getJSONObject("current").getString("last_updated"),
            mainObject.getJSONObject("current").getJSONObject("condition").getString("text"),
            mainObject.getJSONObject("current").getString("temp_c"),
            weatherItem.maxTemp,
            weatherItem.minTemp,
            mainObject.getJSONObject("current").getJSONObject("condition").getString("icon"),
            weatherItem.hours
        )
        model.lifeDataCurrent.value = item
//        Log.d("MyLog", item.maxTemp)
//        Log.d("MyLog", item.minTemp)
//        Log.d("MyLog", item.hours)
    }

    private fun updateCurrentCard() = with(binding) {
        model.lifeDataCurrent.observe(viewLifecycleOwner) {
            val temp = "${it.maxTemp} C / ${it.minTemp} C"
            tViewTime.text = it.time
            tViewCity.text = it.city
            tViewCond.text = it.condition
            tViewTemp.text = it.currentTemp
            tViewMaxMinTemp.text = temp
            Picasso.get().load("https:" + it.imageUrl).into(imView)
        }
    }

    private fun initViewPager() = with(binding) {
        val viewPagerAdapter = ViewPagerAdapter(activity as FragmentActivity, fragmentList)
        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = textList[position]

        }.attach()
    }

    private fun permissionListener() {
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                Toast.makeText(activity, "Permission is $it", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(activity, "Permission is $it", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionListener()
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}