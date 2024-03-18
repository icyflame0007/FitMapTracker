package com.example.fitmaptracker.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import com.example.fitmaptracker.R

class TdeeActivity : AppCompatActivity() {

    lateinit var  edt_height : EditText
    lateinit var  edt_weight : EditText
    lateinit var  edt_age : EditText
    lateinit var  activityLevelSpinner :Spinner
    lateinit var  btn_calculate :Button
    lateinit var  tdeeTextView :TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tdee)
        enableEdgeToEdge()

        edt_age =findViewById(R.id.edt_age)
        edt_height =findViewById(R.id.edt_height)
        edt_weight =findViewById(R.id.edt_weight)
        activityLevelSpinner = findViewById(R.id.activityLevelSpinner)
        btn_calculate = findViewById(R.id.btn_calculate)
        tdeeTextView =findViewById(R.id.tdeeTextView)


        val activityLevels = arrayOf("Sedentary", "Lightly active", "Moderately active", "Very active")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, activityLevels)
        activityLevelSpinner.adapter = adapter

        btn_calculate.setOnClickListener {
            val age = edt_age.text.toString().toInt()
            val weight = edt_weight.text.toString().toFloat()
            val height = edt_height.text.toString().toFloat()
            val activityLevel = when (activityLevelSpinner.selectedItemPosition) {
                0 -> 1.2f // Sedentary
                1 -> 1.375f // Lightly active
                2 -> 1.55f // Moderately active
                3 -> 1.725f // Very active
                else -> 1.2f
            }

            if (age != 0 && weight != 0f && height != 0f) {
                val tdee = calculateTDEE(age, weight, height, activityLevel)
                tdeeTextView.text = "Your TDEE: $tdee"
            }
        }
    }

    private fun calculateTDEE(age: Int, weight: Float, height: Float, activityLevel: Float): Float {
        // Using Mifflin-St Jeor Equation
        val bmr = 10 * weight + 6.25f * height - 5 * age
        return bmr * activityLevel
    }
}