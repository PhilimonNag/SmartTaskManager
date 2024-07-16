package com.philimonnag.smarttaskmanager.ui.screen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.philimonnag.smarttaskmanager.ui.components.PieChart
import com.philimonnag.smarttaskmanager.ui.viewmodels.TaskStatisticsViewModel

@Composable
fun DashboardScreen(taskStatisticsViewModel: TaskStatisticsViewModel) {
    val taskStats by taskStatisticsViewModel.taskStats.observeAsState()
    LaunchedEffect(taskStats) {
        taskStatisticsViewModel.calculateTaskStats()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PieChart(
            radiusOuter = 100.dp,
            data = mapOf(
                Pair("Completed", taskStats!!.completedTasks),
                Pair("Pending", taskStats!!.pendingTasks),
            )
        )
    }
}