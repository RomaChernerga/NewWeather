package com.example.newweatherapp.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.newweatherapp.DialogManager
import com.example.newweatherapp.MainViewModel
import com.example.newweatherapp.adapters.ViewPagerAdapter
import com.example.newweatherapp.adapters.WeatherModel
import com.example.newweatherapp.databinding.FragmentMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
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
    private lateinit var fLocationClient : FusedLocationProviderClient

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
        initLocationClient()
//        requestWeatherData("Moscow", 3)

    }

    override fun onResume() {
        super.onResume()
        checkLocation()
    }

    private fun initLocationClient() = with(binding) {
        fLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        imViewRefresh.setOnClickListener {
            tabLayout.selectTab(tabLayout.getTabAt(0))
            checkLocation()
        }
    }
    private fun checkNewCity() = with(binding) {
        imViewFind.setOnClickListener {
            DialogManager.setCity(requireContext(),object :DialogManager.Listener {
                override fun onClick() {

                }

            })
        }
    }


    private fun checkLocation() {
        if (isLocationEnabled()) {
            getLocation()
        } else {
            DialogManager.locationSettingsDialog(requireContext(),object : DialogManager.Listener{
                override fun onClick() {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }

            })
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun getLocation() {
        val ct = CancellationTokenSource()
        val token = ct.token
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, token)
            .addOnCompleteListener {
                requestWeatherData("${it.result.latitude},${it.result.longitude}", 3)
            }

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
                /** получаем стрингу - это JSON объект который потом передаем в метод */
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
        val list = parseAllDays(mainObject)
        parseCurrentData(mainObject, list[0])
        model.lifeDataList.value = list
    }

    /** Запрос данных списка погоды по дням  */
    private fun parseAllDays(mainObject: JSONObject): List<WeatherModel> {
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
                day.getJSONObject("day").getString("maxtemp_c").toFloat().toInt().toString(),
                day.getJSONObject("day").getString("mintemp_c").toFloat().toInt().toString(),
                day.getJSONObject("day").getJSONObject("condition").getString("icon"),
                day.getJSONArray("hour").toString()
            )
            dayList.add(dayItem)
        }
        model.lifeDataList.value = dayList
        return dayList
    }

    /** Запрос данных для карточки на день  */
    private fun parseCurrentData(mainObject: JSONObject, weatherItem: WeatherModel) {
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
    }

    /** Обновление карточки с погодой на сегодня */
    private fun updateCurrentCard() = with(binding) {
        model.lifeDataCurrent.observe(viewLifecycleOwner) {
            val maxMinTemp = "${it.maxTemp} C / ${it.minTemp} C"
            tViewTime.text = it.time
            tViewCity.text = it.city
            tViewTemp.text = it.currentTemp.ifEmpty { maxMinTemp }
            tViewCond.text = it.condition
            tViewMaxMinTemp.text = if (it.currentTemp.isEmpty())"" else maxMinTemp
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