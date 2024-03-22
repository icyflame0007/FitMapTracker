package com.example.fitmaptracker.blood

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.fitmaptracker.R


import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.FirebaseDatabase

class DonateBloodActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var bloodGroupEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var ageEditText: EditText
    private lateinit var phoneNumberEditText: EditText

    private lateinit var submitButton: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val REQUEST_LOCATION_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donate_blood)
        enableEdgeToEdge()

        nameEditText = findViewById(R.id.nameEditText)
        bloodGroupEditText = findViewById(R.id.bloodGroupEditText)
        genderRadioGroup = findViewById(R.id.genderRadioGroup)
        ageEditText = findViewById(R.id.ageEditText)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        submitButton = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {

            val name = nameEditText.text.toString().trim()
            val bloodGroup = bloodGroupEditText.text.toString().trim()
            val genderId = genderRadioGroup.checkedRadioButtonId
            val gender = findViewById<RadioButton>(genderId).text.toString()
            val age = ageEditText.text.toString().trim().toIntOrNull() ?: 0
            val phoneNumber = phoneNumberEditText.text.toString().trim()

            Log.d("TAGOK","${name}")
            Log.d("TAGOK","${bloodGroup}")
            Log.d("TAGOK","${gender}")
            Log.d("TAGOK","${age}")
            Log.d("TAGOK","${phoneNumber}")


            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //  TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.


                ActivityCompat.requestPermissions(this,
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    REQUEST_LOCATION_PERMISSION
                )
            }
            else{


            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    val latitude = location?.latitude ?: 0.0
                    val longitude = location?.longitude ?: 0.0
                    val locationString = "$latitude,$longitude"

                    val donor =
                        DonarModel(name, bloodGroup, gender, age, phoneNumber, locationString)
                    val database = FirebaseDatabase.getInstance().reference.child("donors").push()
                    database.setValue(donor)
                        .addOnSuccessListener {
                            clearFields()
                            startActivity(Intent(this, MapsActivity::class.java))
                        }
                        .addOnFailureListener {
                            // Handle failure
                        }

                }
            }



        }
    }

    private fun clearFields() {
        nameEditText.text.clear()
        bloodGroupEditText.text.clear()
        genderRadioGroup.clearCheck()
        ageEditText.text.clear()
        phoneNumberEditText.text.clear()

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
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        val latitude = location?.latitude ?: 0.0
                        val longitude = location?.longitude ?: 0.0
                        val locationString = "$latitude,$longitude"

                        val donor = DonarModel(
                            nameEditText.text.toString().trim(),
                            bloodGroupEditText.text.toString().trim(),
                            findViewById<RadioButton>(genderRadioGroup.checkedRadioButtonId).text.toString(),
                            ageEditText.text.toString().trim().toIntOrNull() ?: 0,
                            phoneNumberEditText.text.toString().trim(),
                            locationString
                        )
                        val database =
                            FirebaseDatabase.getInstance().reference.child("donors").push()
                        database.setValue(donor)
                            .addOnSuccessListener {
                                clearFields()
                                startActivity(Intent(this, MapsActivity::class.java))
                            }
                            .addOnFailureListener {
                                // Handle failure
                            }
                    }
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
