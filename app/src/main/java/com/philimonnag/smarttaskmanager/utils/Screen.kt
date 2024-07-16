package com.philimonnag.smarttaskmanager.utils

import androidx.annotation.DrawableRes
import com.philimonnag.smarttaskmanager.R

sealed class Screen(val route:String, @DrawableRes val icon:Int, val title:String ){
    data object Home: Screen("home", R.drawable.baseline_home_filled_24,"Home")
    data object DashBoard: Screen("dashboard", R.drawable.baseline_dashboard_24,"Dashboard")
    data object Settings: Screen("Settings", R.drawable.baseline_settings_24,"Settings")
    data object TaskDetails: Screen("taskDetails", R.drawable.baseline_details_24,"Details")
    data object AddOrEdit: Screen("addOrEdit", R.drawable.baseline_edit_24,"Add/Edit")
    data object GoogleMAP:Screen("Map",R.drawable.baseline_details_24,"Map")
    data object BiometricScreen: Screen("Biometric", R.drawable.baseline_details_24,"Biometric")
}