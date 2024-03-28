package com.example.fitmaptracker.blood

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.fitmaptracker.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.fitmaptracker.databinding.ActivityMapsBinding
import com.example.fitmaptracker.other.Constants.hasLocationPermissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.model.Marker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,GoogleMap.OnMarkerClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMarkerClickListener(this)





            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
        mMap.isMyLocationEnabled = true

        val database = FirebaseDatabase.getInstance().reference.child("donors")
        val valueEventListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val donor = snapshot.getValue(DonarModel::class.java)
                    if (donor != null) {
                        val latLng = LatLng(
                            donor.location?.split(",")?.get(0)?.toDouble() ?: 0.0,
                            donor.location?.split(",")?.get(1)?.toDouble() ?: 0.0
                        )
                        mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(donor.name)
                                .snippet("Blood Group: ${donor.bloodGroup}")

                        )?.tag = donor
                    }
                }
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    val currentLatLng = LatLng(
                        location?.latitude ?: 0.0,
                        location?.longitude ?: 0.0
                    )


                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 20f))
                }
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@MapsActivity,
                    "Error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        database.addValueEventListener(valueEventListener)


    }


    private fun showDonorDetails(donor: DonarModel) {

        val dialogView = layoutInflater.inflate(R.layout.alert_dialog_blood, null)
        val donorName = dialogView.findViewById<TextView>(R.id.donor_name)
        val donorBloodGroup = dialogView.findViewById<TextView>(R.id.donor_blood_group)
        val donorGender = dialogView.findViewById<TextView>(R.id.donor_gender)
        val donorAge = dialogView.findViewById<TextView>(R.id.donor_age)
        val donorPhoneNumber = dialogView.findViewById<TextView>(R.id.donor_phone_number)

        donorName.text = donor.name
        donorBloodGroup.text = donor.bloodGroup
        donorGender.text = donor.gender
        donorAge.text = donor.age.toString()
        donorPhoneNumber.text = donor.phoneNumber

        val callButton = dialogView.findViewById<Button>(R.id.btn_call)
        val cancelButton = dialogView.findViewById<Button>(R.id.btn_cancel)

        val builder = AlertDialog.Builder(this)
            .setView(dialogView)

        val dialog = builder.create()
        val dialogbg : Drawable? = ContextCompat.getDrawable(this,R.drawable.alert_dialog_shape)
        dialog.window?.setBackgroundDrawable(dialogbg)



        callButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${donor.phoneNumber}")
            startActivity(intent)
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                mMap.isMyLocationEnabled = true
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onMarkerClick(marker: Marker): Boolean {

        val donor = marker.tag as? DonarModel
        if (donor != null) {
            showDonorDetails(donor)
            return true
        }
        return false

    }

}


