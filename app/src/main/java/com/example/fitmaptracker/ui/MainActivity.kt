package com.example.fitmaptracker.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.fitmaptracker.R
import com.example.fitmaptracker.R.id.navHostFragment
import com.example.fitmaptracker.blood.DonateBloodActivity
import com.example.fitmaptracker.blood.MapsActivity
import com.example.fitmaptracker.db.RunDAO
import com.example.fitmaptracker.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import nl.joery.animatedbottombar.AnimatedBottomBar
import javax.inject.Inject

@AndroidEntryPoint

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var bottom_navigation : AnimatedBottomBar
    private lateinit var  navHostFragment : View
    private lateinit var  toolbar : Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)



        toolbar = findViewById(R.id.toolbar)
        bottom_navigation = findViewById(R.id.bottom_navigation)
        navHostFragment = findViewById(R.id.navHostFragment)


        toolbar.title = "FitMap Tracker"
        setSupportActionBar(toolbar)
        navController = findNavController(R.id.navHostFragment)
        setupActionBarWithNavController(navController)




        navHostFragment.findNavController()
            .addOnDestinationChangedListener(object:
            NavController.OnDestinationChangedListener{
                override fun onDestinationChanged(
                    controller: NavController,
                    destination: NavDestination,
                    arguments: Bundle?
                ) {
                   when(destination.id)
                   {

                       R.id.settingsFragment , R.id.runFragment, R.id.statisticsFragment->
                           bottom_navigation.visibility = View.VISIBLE
                       else -> bottom_navigation.visibility = View.GONE
                   }

                    bottom_navigation.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
                        override fun onTabSelected(
                            lastIndex: Int,
                            lastTab: AnimatedBottomBar.Tab?,
                            newIndex: Int,
                            newTab: AnimatedBottomBar.Tab
                        ) {
                            if(newIndex==0)
                            {
                                navController.navigate(R.id.runFragment)
                            }
                            else if(newIndex==1)
                            {
                                navController.navigate(R.id.statisticsFragment)
                            }
                            else if(newIndex==2)
                            {
                                navController.navigate(R.id.settingsFragment)
                            }
                            else if(newIndex==3)
                            {
                               startActivity(Intent(this@MainActivity,DonateBloodActivity::class.java))
                            }
                            else
                            {
                                startActivity(Intent(this@MainActivity,MapsActivity::class.java))
                            }
                        }


                    })

                }


            })

        navigateToTrackingFragmentIfNeeded(intent)



    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        bottom_navigation.setupWithNavController(menu!!, navController)


        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return true
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }


    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?)
    {
        if(intent?.action == ACTION_SHOW_TRACKING_FRAGMENT)
        {
            navHostFragment.findNavController().navigate(R.id.action_global_trackingFragment)
        }
    }

}