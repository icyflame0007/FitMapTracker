package com.example.fitmaptracker.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.fitmaptracker.ui.activities.BmiActivity
import com.example.fitmaptracker.R
import com.example.fitmaptracker.blood.DonateBloodActivity
import com.example.fitmaptracker.blood.MapsActivity
import com.example.fitmaptracker.other.Constants
import com.example.fitmaptracker.other.Constants.EMPTY_STRING
import com.example.fitmaptracker.other.Constants.KEY_NAME
import com.example.fitmaptracker.other.Constants.KEY_WEIGHT
import com.example.fitmaptracker.ui.activities.TdeeActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {
    @Inject
    lateinit var appPreferences: SharedPreferences


    lateinit var et_name :EditText
    lateinit var et_weight:EditText
    lateinit var  btn_apply_changes : Button
    lateinit var bmi_card_view :CardView
    lateinit var  tdee_card_view :CardView

    lateinit var btn_donate :Button
    lateinit var  btn_request : Button




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        et_name = view.findViewById(R.id.et_name)
        et_weight = view.findViewById(R.id.et_weight)
        btn_apply_changes =view.findViewById(R.id.btn_apply_changes)
        bmi_card_view =view.findViewById(R.id.bmiCardView)
        tdee_card_view = view.findViewById(R.id.tdeeCardView)
        btn_donate = view.findViewById(R.id.btn_donate)
        btn_request = view.findViewById(R.id.btn_request)

        btn_apply_changes.setOnClickListener {
            if(!(et_name.text.isNullOrEmpty() || et_weight.text.isNullOrEmpty())){
                appPreferences.edit()
                    .putString(KEY_NAME,et_name.text.toString())
                    .putString(Constants.KEY_WEIGHT,et_weight.text.toString())
                    .apply()
                Snackbar.make(it,"Changes saved successfully", Snackbar.LENGTH_SHORT).show()
            }
            else Snackbar.make(it,"All fields must be filled", Snackbar.LENGTH_SHORT).show()
        }
        et_name.setText(appPreferences.getString(KEY_NAME, EMPTY_STRING))
        et_weight.setText(appPreferences.getString(KEY_WEIGHT, EMPTY_STRING))

        bmi_card_view.setOnClickListener {

            val intent = Intent(context, BmiActivity::class.java)
            startActivity(intent)

        }


        tdee_card_view.setOnClickListener {

            val intent = Intent(context, TdeeActivity::class.java)
            startActivity(intent)

        }

        btn_donate.setOnClickListener {
            startActivity(Intent(context,DonateBloodActivity::class.java))
        }

        btn_request.setOnClickListener {

            startActivity(Intent(context,MapsActivity::class.java))
        }


        }
    }
