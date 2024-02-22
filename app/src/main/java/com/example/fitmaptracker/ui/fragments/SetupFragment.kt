package com.example.fitmaptracker.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fitmaptracker.R

class SetupFragment : Fragment(R.layout.fragment_setup) {

    lateinit var tvContinue : TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tvContinue =view.findViewById(R.id.tvContinue)

        tvContinue.setOnClickListener {
            findNavController().navigate(R.id.action_setupFragment_to_runFragment)
        }

    }
}