package com.example.listycity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CityRepository {
    private val db = Firebase.firestore
    private val citiesRef = db.collection("cities")
    private val _cities = mutableStateListOf<City>()

    var errorMessage by mutableStateOf<String?>(null)
        private set

    val cities: List<City>
        get() = _cities

    private val listener = citiesRef.addSnapshotListener { snapshot, error ->
        if (error != null) {
            errorMessage = "Could not load cities: ${error.localizedMessage}"
            return@addSnapshotListener
        }

        val savedCities = snapshot?.documents?.mapNotNull { it.toObject(City::class.java) }
            ?: return@addSnapshotListener
        _cities.clear()
        _cities.addAll(savedCities)
    }

    fun addCity(city: City) {
        errorMessage = null
        // Keep document IDs independent of names so renaming a city is safe.
        citiesRef.add(city).addOnFailureListener {
            errorMessage = "Could not add city: ${it.localizedMessage}"
        }
    }

    fun updateCity(oldCity: City, updatedCity: City) {
        errorMessage = null
        citiesRef.document(oldCity.documentId).set(updatedCity).addOnFailureListener {
            errorMessage = "Could not update city: ${it.localizedMessage}"
        }
    }

    fun deleteCity(city: City) {
        errorMessage = null
        citiesRef.document(city.documentId).delete().addOnFailureListener {
            errorMessage = "Could not delete city: ${it.localizedMessage}"
        }
    }

    fun close() {
        listener.remove()
    }
}
