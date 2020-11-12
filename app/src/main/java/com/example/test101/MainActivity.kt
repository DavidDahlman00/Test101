package com.example.test101

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val REQUEST_LOCATION = 1
    lateinit var locationProvider: FusedLocationProviderClient
    lateinit var addButton: Button
    lateinit var addText: EditText
    lateinit var textview: TextView

    var locationRequest : LocationRequest? = null
    lateinit var locationCallback: LocationCallback

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationProvider = getFusedLocationProviderClient(this)

        textview = findViewById(R.id.textview)
        textview.visibility = View.INVISIBLE
        addButton = findViewById(R.id.add_button)
        addText = findViewById(R.id.addItemText)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        locationCallback = MyCostumLocationCallback()

        val db = FirebaseFirestore.getInstance()
        val dataBasItems = mutableListOf<DropItem>()
        val itemsRef = db.collection("tests")

        updateText(dataBasItems, itemsRef)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = DropSpotRecyclerAdapter(this, DataManager.dropSpots)

        if( ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            Log.d("!!!", "no permission")

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION)
        } else {

        }
        addButton.setOnClickListener {
            addItem(dataBasItems, itemsRef)
        }

        locationRequest = creatLocationRequest()

    }

    fun updateText(dataBasItems: MutableList<DropItem>, itemsRef: CollectionReference) {
        dataBasItems.clear()
        DataManager.dropSpots.clear()
        itemsRef.get().addOnSuccessListener { documentSnapshot ->
            for (document in documentSnapshot){
                val newItem = document.toObject(DropItem::class.java)
                if (newItem != null){
                    dataBasItems.add(newItem)
                    DataManager.dropSpots.add(newItem)
                }


                Log.d("!!!", "${newItem}")


            }
            Log.d("!!!", "List1: ${dataBasItems.size}")
            textview.text = ""
            for (elem in DataManager.dropSpots){
                var tmp = textview.text.toString()
                tmp += "\n" + elem.name + ": " + elem.lat + ": " + elem.lng + ": " + elem.category + ": " + getDateTime(elem.time.toString())
                textview.text = tmp
            }
        }
    }

    fun addItem(dataBasItems: MutableList<DropItem>, itemsRef: CollectionReference){

        val tmp = addText.text.toString()
        if( ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {

        locationProvider.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val lng = location.longitude
                Log.d("!!!", "lat: $lat, lng: $lng")
                val db = FirebaseFirestore.getInstance()
                val time = System.currentTimeMillis().toString()

                val item1 = DropItem(tmp, lat, lng, "test", time)
                db.collection("tests").add(item1)
                textview.text = item1.toString()
                }
            }.addOnSuccessListener{
            updateText(dataBasItems, itemsRef)
            }
        }


    }

    private fun getDateTime(s: String): String? {
        try {
            val sdf = SimpleDateFormat("yyyy/MM/dd")
            val netDate = Date(s.toLong())
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }

    }

    override fun onResume() {
        super.onResume()

        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()

        stopLocationUpdates()
    }


    fun startLocationUpdates() {
        if( ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
            locationProvider.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    fun stopLocationUpdates() {
        locationProvider.removeLocationUpdates(locationCallback)
    }

    fun creatLocationRequest()  =
        LocationRequest.create().apply{
            interval = 2000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUEST_LOCATION ) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                Log.d("!!!", "Permission granted")
                startLocationUpdates()
            } else {
                Log.d("!!!","Permission denied")
            }
        }
    }
}