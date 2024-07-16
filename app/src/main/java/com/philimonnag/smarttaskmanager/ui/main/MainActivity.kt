package com.philimonnag.smarttaskmanager.ui.main

import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.text.TextUtils.TruncateAt
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.philimonnag.smarttaskmanager.data.local.TaskDB
import com.philimonnag.smarttaskmanager.data.repository.TaskRepository
import com.philimonnag.smarttaskmanager.ui.screen.AddOrEditScreen
import com.philimonnag.smarttaskmanager.ui.screen.BiometricAuthScreen
import com.philimonnag.smarttaskmanager.ui.screen.DashboardScreen
import com.philimonnag.smarttaskmanager.ui.screen.GoogleMapScreen
import com.philimonnag.smarttaskmanager.utils.Screen
import com.philimonnag.smarttaskmanager.ui.screen.HomeScreen
import com.philimonnag.smarttaskmanager.ui.screen.SettingScreen
import com.philimonnag.smarttaskmanager.ui.screen.TaskDetailsScreen
import com.philimonnag.smarttaskmanager.ui.theme.SmartTaskManagerTheme
import com.philimonnag.smarttaskmanager.ui.viewmodels.TaskStatisticsViewModel
import com.philimonnag.smarttaskmanager.ui.viewmodels.TaskViewModel
import com.philimonnag.smarttaskmanager.ui.viewmodels.ThemeViewModel

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@Composable
fun App(themeViewModel:ThemeViewModel= viewModel()){
    val isDarkTheme=themeViewModel.isDarkTheme.observeAsState(initial = false)
    SmartTaskManagerTheme(darkTheme = isDarkTheme.value) {
        val ctx= LocalContext.current.applicationContext
        val db=TaskDB.getInstance(ctx)
        val taskRepository=TaskRepository(db)
        val taskViewModel=TaskViewModel(taskRepository)
        val taskStatsViewModel=TaskStatisticsViewModel(taskRepository)
        TaskApp(
            themeViewModel = themeViewModel,
            taskViewModel=taskViewModel,
            taskStatisticsViewModel=taskStatsViewModel
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskApp(themeViewModel: ThemeViewModel,
            taskViewModel: TaskViewModel,
            taskStatisticsViewModel: TaskStatisticsViewModel
            ) {
    val navController= rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.DashBoard,
        Screen.Settings
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    if (showBackButton(currentDestination?.route)) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                title = {
                    Text(text = getTitleForRoute(currentDestination?.route))
                })
        },
        floatingActionButton = {
             if(showFloatingActionBtn(currentDestination?.route))
             {
                 FloatingActionButton(onClick = {navController.navigate(Screen.AddOrEdit.route+"/0")}) {
                    Text(text = "Add")
             }
            }
        },
        bottomBar = {
            Log.d("TAG","route is: "+currentDestination?.route)
            if(showBottomBar(currentDestination?.route)){
                BottomNavigation (
                    modifier = Modifier.padding(bottom = 36.dp),
                    backgroundColor = Color.White
                ){
                    items.forEachIndexed { index, screen ->
                        val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                        BottomNavigationItem(
                            icon = {
                                Box(
                                    modifier = Modifier
                                        .size(70.dp) // Adjust size as needed
                                        .clip(CircleShape)
                                        .background(if (isSelected) Color.Black else Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = screen.icon),
                                        contentDescription = screen.title,
                                        modifier = Modifier
                                            .size(46.dp) // Adjust size as needed
                                            .padding(12.dp),
                                        tint = if (isSelected) Color.White else Color.Black
                                    )
                                }
                            },
                           label = { Text(screen.title, modifier = Modifier.padding(vertical = 12.dp)) },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }

    ) { innerPadding ->

        NavHost(modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Screen.BiometricScreen.route) {
            composable(Screen.BiometricScreen.route){
                BiometricAuthScreen(onLoginSuccess = {
                    navController.navigate(Screen.Home.route)
                })
            }
            composable(Screen.Home.route){
                HomeScreen(taskViewModel,navController)
            }
            composable(Screen.Settings.route){
                SettingScreen(
                    themeViewModel = themeViewModel
                )
            }
            composable(Screen.DashBoard.route){
                DashboardScreen(taskStatisticsViewModel = taskStatisticsViewModel)
            }
            composable(Screen.AddOrEdit.route+"/{taskId}"){
                val taskId=it.arguments?.getString("taskId")?.toInt()

                AddOrEditScreen( taskId = taskId, taskViewModel = taskViewModel,navController)
            }
            composable(Screen.TaskDetails.route+"/{taskId}"){
                val taskId=it.arguments?.getString("taskId")?.toInt()
               TaskDetailsScreen(taskId = taskId, viewModel = taskViewModel)
            }
            composable(Screen.GoogleMAP.route+"/{lat}/{long}"){
             it ->
                val latitude = it.arguments?.getString("lat")?.toDoubleOrNull()
                val longitude = it.arguments?.getString("long")?.toDoubleOrNull()
                GoogleMapScreen(navController, latitude, longitude)
            }

        }


    }
}





fun getTitleForRoute(route: String?): String {
    return when (route) {
        Screen.BiometricScreen.route->Screen.BiometricScreen.title
        Screen.GoogleMAP.route+"/{lat}/{long}"->Screen.GoogleMAP.title
        Screen.Home.route -> Screen.Home.title
        Screen.DashBoard.route -> Screen.DashBoard.title
        Screen.Settings.route -> Screen.Settings.title
        Screen.TaskDetails.route+"/{taskId}" -> Screen.TaskDetails.title
        Screen.AddOrEdit.route+"/{add}" -> "Add"
        Screen.AddOrEdit.route+"/{taskId}"->"Edit"
        else -> "Unknown"
    }
}

fun showBottomBar(route: String?):Boolean{
    return  when(route){
        Screen.BiometricScreen.route ->false
        Screen.GoogleMAP.route+"/{lat}/{long}"->false
        Screen.TaskDetails.route+"/{taskId}" -> false
        Screen.AddOrEdit.route+"/{add}" -> false
        Screen.AddOrEdit.route+"/{taskId}"->false
        else->true
    }
}
fun showFloatingActionBtn(route: String?):Boolean{
    return route==Screen.Home.route
}

fun showBackButton(route: String?):Boolean{
    return  when(route){
        Screen.GoogleMAP.route+"/{lat}/{long}"->true
        Screen.TaskDetails.route+"/{taskId}" -> true
        Screen.AddOrEdit.route+"/0" -> true
        Screen.AddOrEdit.route+"/{taskId}"->true
        else->false
    }
}