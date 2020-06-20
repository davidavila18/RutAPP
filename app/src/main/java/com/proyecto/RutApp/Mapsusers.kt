package com.proyecto.RutApp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class Mapsusers : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var fusedlocationclient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE=1
    }

    override fun onMarkerClick(p0: Marker?) = false
    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapsusers)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedlocationclient= LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener (this)
        mMap.uiSettings.isZoomControlsEnabled = true
        setUpMap()
    }

    //solicitar permiso
    private fun setUpMap() {
        if(ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedlocationclient.lastLocation.addOnSuccessListener (this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 15f))
            }
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("¿Desea Cerrar Sesion?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent(this@Mapsusers, inicio::class.java)
                startActivity(intent)
            })
                    .setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            builder.show()
        }
        return super.onKeyDown(keyCode, event)
    }
}