package com.philimonnag.smarttaskmanager.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.philimonnag.smarttaskmanager.data.local.TaskEntity
import com.philimonnag.smarttaskmanager.ui.theme.highPriorityColor
import com.philimonnag.smarttaskmanager.ui.theme.lowPriorityColor
import com.philimonnag.smarttaskmanager.ui.theme.mediumPriorityColor
import com.philimonnag.smarttaskmanager.ui.viewmodels.TaskViewModel
import com.philimonnag.smarttaskmanager.utils.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(taskViewModel: TaskViewModel,navController: NavController) {
    var searchText by remember {
        mutableStateOf("")
    }
    val taskz by taskViewModel.tasks.collectAsState(initial = emptyList())
    LazyColumn(modifier = Modifier
        .fillMaxHeight()
        .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { 
            Row {
                TextField(
                    value = searchText,
                    onValueChange = {it->searchText=it},
                    placeholder = { Text(text = "Search Task Here...")}
                )
                IconButton(onClick = { taskViewModel.search(searchText)}) {
                  Icon(imageVector = Icons.Default.Search, contentDescription ="search")
                }
            }
        }

        items(taskz){it->
            TaskCard(task = it, viewModel =taskViewModel, navController = navController )
        }
    }
}

@Composable
fun TaskCard(task:TaskEntity,viewModel: TaskViewModel,navController: NavController) {
    Card (
        colors = CardDefaults.cardColors(
            containerColor = Color.White
//        containerColor = when(task.priority){
//            1-> lowPriorityColor
//            2-> mediumPriorityColor
//            else-> highPriorityColor
//        },

        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        )){
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable {
                navController.navigate(Screen.TaskDetails.route + "/${task.id}")
            }
        ){
            Box(modifier = Modifier
                .background( color=when(task.priority){
                1-> mediumPriorityColor
                2->  lowPriorityColor
                else-> highPriorityColor
            }, shape = RoundedCornerShape(12.dp)
                )) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange ={isChecked->
                        viewModel.updateTask(TaskEntity(task.id,
                            task.title,
                            task.desc,
                            task.priority,
                            task.location,
                            task.desc,
                            isChecked))
                    } )
            }

            Column {
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = task.title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 22.sp,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = task.desc,
                    fontWeight = FontWeight.Light,
                    fontSize = 20.sp,
                    color = Color.Gray
                )
//                Text(text = task.title)
//                Text(text = task.desc)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { navController.navigate(Screen.AddOrEdit.route+"/${task.id}") }) {
                Icon(imageVector = Icons.Default.Edit,
                    tint = Color.Black,
                    contentDescription = "edit")
            }
            IconButton(onClick = { viewModel.deleteTask(task) }) {
                Icon(imageVector = Icons.Default.Delete,
                    tint = Color.Black,
                    contentDescription = "Delete")
            }
        }
    }
}