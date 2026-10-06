package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.runtime.DisposableEffect
import com.example.listycity.ui.theme.ListyCityTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository()

        setContent {
            DisposableEffect(cityRepository) {
                onDispose { cityRepository.close() }
            }
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = { cityRepository.addCity(it) },
                        onUpdateCity = { oldCity, updatedCity ->
                            cityRepository.updateCity(oldCity, updatedCity)
                        },
                        onDeleteCity = { cityRepository.deleteCity(it) },
                        errorMessage = cityRepository.errorMessage,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
