package com.example.test101

import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult

class MyCostumLocationCallback : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult) {
        for (location in locationResult.locations){
            Log.d("!!!", "Lat: ${location.latitude}, Lng: ${location.longitude}")
        }
    }
}