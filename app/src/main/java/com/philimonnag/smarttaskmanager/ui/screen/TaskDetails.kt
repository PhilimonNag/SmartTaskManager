package com.philimonnag.smarttaskmanager.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.philimonnag.smarttaskmanager.ui.viewmodels.TaskViewModel
import com.philimonnag.smarttaskmanager.utils.Priority

@Composable
fun TaskDetailsScreen(taskId: Int?,
                      viewModel: TaskViewModel) {
    val task =viewModel.getTaskById(taskId ?: 0).observeAsState().value

    task?.let {it->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)) {
            Text(text = "Title: ${it.title}")
            Text(text = "Description: ${it.desc}")
            Text(text = "Priority: ${Priority.entries[it.priority].name}")
            Text(text = "Location: ${it.location}")
            Text(text = "Due Date: ${it.dueDate}")
            Text(text = "Completed: ${it.isCompleted}")
        }
    }
}