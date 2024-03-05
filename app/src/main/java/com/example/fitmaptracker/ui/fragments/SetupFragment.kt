package com.example.fitmaptracker.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.fitmaptracker.R
import com.example.fitmaptracker.R.id.action_setupFragment_to_runFragment
import com.example.fitmaptracker.other.Constants.EMPTY_STRING
import com.example.fitmaptracker.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.fitmaptracker.other.Constants.KEY_NAME
import com.example.fitmaptracker.other.Constants.KEY_WEIGHT
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {

    lateinit var tvContinue : TextView
    lateinit var etName :EditText
    lateinit var  etWeight : EditText

    @Inject
    lateinit var appPref: SharedPreferences

    @set:Inject
    var isFirstRun : Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tvContinue =view.findViewById(R.id.tvContinue)
        etName = view.findViewById(R.id.etName)
        etWeight =  view.findViewById(R.id.etWeight)
        val editor = appPref.edit()

        if(!isFirstRun)
        {

            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment,true)
                .build()
            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
                savedInstanceState,
                navOptions
            )

        }
        saveInPref()
        etWeight.setText(appPref.getString(KEY_WEIGHT, EMPTY_STRING))
        etName.setText(appPref.getString(KEY_NAME,EMPTY_STRING))

        tvContinue.setOnClickListener{
//            findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            if(!etWeight.text.isNullOrEmpty()){
                editor.putString(KEY_WEIGHT,etWeight.text.toString())
            }
            if(!etName.text.isNullOrEmpty()){
                editor.putString(KEY_NAME,etName.text.toString())
            }
            editor.apply()
        }

    }
    private fun saveInPref() = appPref.edit().putBoolean(KEY_FIRST_TIME_TOGGLE,false).apply()
}