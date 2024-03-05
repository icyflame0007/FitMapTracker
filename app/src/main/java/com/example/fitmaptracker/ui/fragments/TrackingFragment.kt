package com.example.fitmaptracker.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fitmaptracker.R
import com.example.fitmaptracker.db.Run
import com.example.fitmaptracker.other.Constants
import com.example.fitmaptracker.other.Constants.ACTION_PAUSE_SERVICE
import com.example.fitmaptracker.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.fitmaptracker.other.Constants.ACTION_STOP_SERVICE
import com.example.fitmaptracker.other.Constants.KEY_WEIGHT
import com.example.fitmaptracker.other.Constants.POLYLINE_CAMERA_ZOOM
import com.example.fitmaptracker.other.Constants.POLYLINE_COLOR
import com.example.fitmaptracker.other.Constants.POLYLINE_WIDTH
import com.example.fitmaptracker.other.Constants.getPolylineLength
import com.example.fitmaptracker.other.Constants.remove
import com.example.fitmaptracker.other.Constants.show
import com.example.fitmaptracker.services.TrackingService
import com.example.fitmaptracker.ui.viewmodels.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private val viewModel : MainViewModel by viewModels()
    private lateinit var  btntogglerun :Button
    private lateinit var  btn_finish_run :Button
    private lateinit var  btn_cancel_run :Button
    private lateinit var  tvTimer :TextView


    @Inject
    lateinit var appPreferences: SharedPreferences

    private var pathPoints = mutableListOf(mutableListOf<LatLng>())
    private var isTracking=false


    private var map: GoogleMap? = null
    private var currentTimeInMillis = 0L

    lateinit var  mapView  : MapView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapView)
        btntogglerun = view.findViewById(R.id.btntogglerun)
        btn_finish_run = view.findViewById(R.id.btn_finish_run)
        btn_cancel_run = view.findViewById(R.id.btn_cancel)
        tvTimer = view.findViewById(R.id.tv_timer)




        btntogglerun.setOnClickListener {
            sendCommmadToService(ACTION_START_OR_RESUME_SERVICE)
        }
        mapView.getMapAsync {
            map = it
            drawAllPolylines()
        }
        mapView.onCreate(savedInstanceState)
        btntogglerun.setOnClickListener {
            if(!isTracking)
            {
                sendCommmadToService(ACTION_START_OR_RESUME_SERVICE)
                btntogglerun.text = "Pause"
                btn_finish_run.remove()
            }
            else{
                sendCommmadToService(ACTION_PAUSE_SERVICE)
                btntogglerun.text="Start"
                btn_finish_run.show()

            }
        }
        btn_finish_run.setOnClickListener {
            endRunAndSaveDb()
            btn_finish_run.remove()
//                tvTimer.text="00:00:00:00"
        }
        btn_cancel_run.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes"){_,_->
                    stopRun()
                }
                .setNegativeButton("No"){dialogInterface,_->
                    dialogInterface.dismiss()
                }.show()
        }

        observeData()


    }



    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
    private fun observeData() {
        lifecycleScope.launch {
            TrackingService.isTracking.collect { isTracking = it }
        }
        lifecycleScope.launch {
            TrackingService.timerInMillis.collect{
                tvTimer.text = Constants.formatMillisToHoursMinutesSecondsMilliseconds(it)
                currentTimeInMillis=it
            }
        }
        TrackingService.pathPoints.observe(viewLifecycleOwner) {
            pathPoints = it
            drawLatestPolylines()
            updateCameraPosition()
        }
    }

    private  fun sendCommmadToService(action: String) =
        Intent(requireContext(),TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)

        }

    private fun drawLatestPolylines(){
        if(pathPoints.isNotEmpty() && pathPoints.last().size>1){
            val latestLine = pathPoints.last()
            val secondLastPoint = latestLine[latestLine.size-2]
            val lastPoint = latestLine.last()
            // polylineOptions describes how the line must look like
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(secondLastPoint)
                .add(lastPoint)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun drawAllPolylines(){
        for(polyline in pathPoints){
            val polylineOptions = PolylineOptions()
                .color(Color.BLUE)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun updateCameraPosition(){
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty())
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(pathPoints.last().last(),POLYLINE_CAMERA_ZOOM))
    }

    private fun stopRun(){
        sendCommmadToService(ACTION_STOP_SERVICE)
        findNavController().run {
            navigate(R.id.action_trackingFragment_to_runFragment2)
            popBackStack(R.id.trackingFragment,true)
        }
    }
    private fun zoomOutForEntirePath(){
        val bounds = LatLngBounds.Builder()
        for(polyline in pathPoints)
            for(pos in polyline)
                bounds.include(pos)

        var distanceM = 0
        for(polyline in pathPoints) distanceM+= getPolylineLength(polyline).toInt()
        val distanceKM = (distanceM/1000f).toInt()
        if(distanceKM>=1)
            try {
                map?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),mapView.width,mapView.height,(mapView.height*0.5f).toInt() ))
            }catch (e:Exception){
                map?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),100 ))
            }
    }
    private fun endRunAndSaveDb(){
        zoomOutForEntirePath()
        val weight : Double=  appPreferences.getString(KEY_WEIGHT,"")?.toDoubleOrNull() ?: 65.0
        map?.snapshot {
            var distanceInMeters = 0
            for(polyline in pathPoints) distanceInMeters+= getPolylineLength(polyline).toInt()
            val timestamp = Calendar.getInstance().timeInMillis
            val caloriesBurned = ((distanceInMeters/1000f)*weight).toInt()
            val avgSpeedInKmph = Math.round((distanceInMeters / 1000f) / (currentTimeInMillis / 1000f / 60 / 60) * 10) /10f
            val run = Run(it,timestamp,avgSpeedInKmph,distanceInMeters,currentTimeInMillis,caloriesBurned)
            viewModel.saveRunInDb(run)
            Snackbar.make(requireActivity().findViewById(R.id.rootView),"saved the run successfully",
                Snackbar.LENGTH_LONG).show()
            stopRun()
        }
    }
}
