package com.example.components.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

object GetLocation {
    private lateinit var locationCallback: LocationCallback
   private lateinit var fusedLocationClient: FusedLocationProviderClient
   private lateinit var locationRequest: LocationRequest
    var location:String = ""
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context, activity: Activity, callback: (location: String) -> Unit) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        ) {
            if (PermissionUtil.requestLocationPermission(activity)) {
                fusedLocationClient.lastLocation.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val location = it.result.longitude.toString() + ":" + it.result.latitude.toString()
                        callback(location)
                    } else {
                        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 50)
                            .setWaitForAccurateLocation(true).setMinUpdateIntervalMillis(30)
                            .setMaxUpdateDelayMillis(100).build()
                        fusedLocationClient.requestLocationUpdates(
                            locationRequest, locationCallback,
                            Looper.myLooper()
                        )
                    }
                }
            }
        }
    }


}