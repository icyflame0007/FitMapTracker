package com.example.fitmaptracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
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
import com.example.fitmaptracker.db.RunDAO
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
                }

            })

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



}