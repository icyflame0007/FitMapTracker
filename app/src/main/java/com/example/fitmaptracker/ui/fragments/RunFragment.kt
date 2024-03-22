package com.example.fitmaptracker.ui.fragments

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitmaptracker.CustomItemAnimator
import com.example.fitmaptracker.R
import com.example.fitmaptracker.adapters.RunAdapter
import com.example.fitmaptracker.other.Constants
import com.example.fitmaptracker.other.Constants.hasLocationPerm
import com.example.fitmaptracker.ui.viewmodels.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject


@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run), EasyPermissions.PermissionCallbacks {


    @Inject
    lateinit var  locationPermissions:Array<String>
    lateinit var  fab :FloatingActionButton
    lateinit var sp_filter :Spinner
    lateinit var tv_filter_by :TextView
    lateinit var  rv_runs :RecyclerView
    lateinit var  rootview :View
    private val viewModel : MainViewModel by viewModels()



    private val runAdapter by lazy { RunAdapter(emptyList()){run->
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Run")
            .setMessage("Are you sure you want to delete this run?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteRun(run)
                Snackbar.make(rootview,"Deleted", BaseTransientBottomBar.LENGTH_LONG).apply {
                    setAction(R.string.undo_deletion){viewModel.saveRunInDb(run)}
                    show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab = view.findViewById(R.id.fab)
        rootview = view.findViewById(R.id.rootview)
        sp_filter =view.findViewById(R.id.sp_filter)
        tv_filter_by = view.findViewById(R.id.tv_filter_by)
        rv_runs =view.findViewById(R.id.rv_runs)


        fab.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }
        requestPerms()

        rv_runs.apply {
            adapter=runAdapter
            layoutManager= LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            itemAnimator = CustomItemAnimator(R.anim.recycler_item_anim)
        }



        observeLiveData()

        sp_filter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                handleSorting(pos)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

    }
    private fun observeLiveData() {
        viewModel.runLiveData.observe(viewLifecycleOwner){runAdapter.submitList(it)}
    }

    private fun handleSorting(pos: Int) {
        viewModel.apply {
            // maintain this order
            when(pos){
                0 -> switchSortingStrategy(Constants.SortingOptions.DATE)
                1 -> switchSortingStrategy(Constants.SortingOptions.TIME)
                2 -> switchSortingStrategy(Constants.SortingOptions.DISTANCE)
                3 -> switchSortingStrategy(Constants.SortingOptions.AVG_SPEED)
                4 -> switchSortingStrategy(Constants.SortingOptions.CALORIES)
            }
        }
    }

    private fun requestPerms(){
        if(!requireContext().hasLocationPerm()) EasyPermissions.requestPermissions(this,"accept the permissions",
            Constants.LOCATION_PERMISSION_REQUEST_CODE,*locationPermissions
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms))
            AppSettingsDialog.Builder(this).build().show()
        else requestPerms()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }
}




