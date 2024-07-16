package com.philimonnag.smarttaskmanager.ui.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.philimonnag.smarttaskmanager.data.local.TaskEntity
import com.philimonnag.smarttaskmanager.ui.viewmodels.TaskViewModel
import com.philimonnag.smarttaskmanager.utils.Priority
import com.philimonnag.smarttaskmanager.utils.Screen
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditScreen(
    taskId:Int?,
    taskViewModel: TaskViewModel,
    navController: NavController) {
    val task = taskViewModel.getTaskById(taskId ?: 0).observeAsState().value

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val latitude by savedStateHandle?.getLiveData<Double>("lat")!!.observeAsState()
    val longitude by savedStateHandle?.getLiveData<Double>("long")!!.observeAsState()
    val locTitle by savedStateHandle?.getLiveData<String>("title")!!.observeAsState()



    Log.d("ADD_OR_EDIT",taskId.toString() +""+task?.title)
    var title by remember {
        mutableStateOf(task?.title?:"")
    }
    var desc by remember {
        mutableStateOf(task?.desc?:"")
    }

    var location by remember {
        mutableStateOf(task?.location?:"")
    }
    var selectedDate by remember { mutableStateOf(task?.dueDate ?: "") }
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var date = remember {
        Calendar.getInstance()
            .timeInMillis
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date,
        yearRange = 1990..2024
    )

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            selectedDate = dateFormat.format(it)
        }
    }
    var selectedPriority by remember {
        mutableStateOf(
            when(task?.priority){
                1->Priority.LOW
                2->Priority.MEDIUM
                else->Priority.HIGH
            }

        )
    }
    LaunchedEffect(task){
        if(task!=null){
            title=task.title
            location=task.location
            desc=task.desc
            selectedDate=task.dueDate
            selectedPriority=when(task?.priority){
                1->Priority.LOW
                2->Priority.MEDIUM
                else->Priority.HIGH
            }
        }
    }
    Column (Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Title")
        TextField(value = title, onValueChange ={it->title=it}, placeholder = { Text(text = "Enter Title here...")})
        Text(text = "Description")
        TextField(value = desc, onValueChange ={it->desc=it}, placeholder = { Text(text = "Enter Description here...")})
        PriorityDropdownMenuBox(selectedPriority = selectedPriority,
            onPrioritySelected = {priority ->  selectedPriority=priority})
        Text(text = "DueDate: $selectedDate")
        Button(onClick = { showDatePicker = !showDatePicker }) {
            Text(text = "Date Picker")
        }


        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDatePicker = false
                            // Update selectedDate based on the DatePickerState
                            selectedDate = dateFormat.format(datePickerState.selectedDateMillis)
                        }
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDatePicker = false }
                    ) { Text("Cancel") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Text(text = "Location $locTitle",
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier =
            Modifier.clickable {
            val lat=20.5937
            val long=78.9629
            navController.navigate(Screen.GoogleMAP.route+"/$lat/$long")
        })
//        TextField(value = location,
//            onValueChange ={it->location=it},
//            placeholder = { Text(text = "Enter Location here...")})
        Button(onClick = {
            val newTask=TaskEntity(taskId?:0,
                title=title,
                desc=desc,
                priority = selectedPriority.ordinal ,
                location=locTitle?:"",
                dueDate=selectedDate,
                isCompleted = false)
            if (task == null) {
                taskViewModel.addTask(newTask)
            }else{
                taskViewModel.updateTask(newTask)
            }

            title=""
            desc=""
            location=""
            navController.popBackStack()
            }) {
            Text(text = if (task == null) "Save" else "Update")
        }
        }


}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityDropdownMenuBox(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedPriority.label,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Priority.values().forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.label) },
                        onClick = {
                            onPrioritySelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
