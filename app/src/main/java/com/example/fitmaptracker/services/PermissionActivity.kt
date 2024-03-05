package com.example.fitmaptracker.services

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.fitmaptracker.R
import com.example.fitmaptracker.other.Constants.ACTION_LOCATION_PERMISSION
import com.example.fitmaptracker.other.Constants.EXTRA_PERMISSION
import com.example.fitmaptracker.other.Constants.LOCATION_PERMISSION_REQUEST_CODE
import com.example.fitmaptracker.other.Constants.hasLocationPerm
import com.example.fitmaptracker.other.Constants.locationPermissions
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class PermissionActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        EasyPermissions.requestPermissions(this,"Accept the permissions",LOCATION_PERMISSION_REQUEST_CODE,*locationPermissions)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if(requestCode== LOCATION_PERMISSION_REQUEST_CODE){
            sendPermissionResult(perms == locationPermissions)
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms))
            AppSettingsDialog.Builder(this).build().show()
        else requestPerms()
    }

    private fun requestPerms(){
        if(hasLocationPerm()) return
        EasyPermissions.requestPermissions(this,"Enable these perms",
            LOCATION_PERMISSION_REQUEST_CODE,*locationPermissions)
    }

    private fun sendPermissionResult(granted: Boolean) {
        val intent = Intent(ACTION_LOCATION_PERMISSION)
        intent.putExtra(EXTRA_PERMISSION, granted)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        finish()
    }
}