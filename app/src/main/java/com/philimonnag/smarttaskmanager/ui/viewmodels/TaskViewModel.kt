package com.philimonnag.smarttaskmanager.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.philimonnag.smarttaskmanager.data.local.TaskEntity
import com.philimonnag.smarttaskmanager.data.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository):ViewModel() {

    fun addTask(taskEntity: TaskEntity){
        viewModelScope.launch {
           taskRepository.addTask(taskEntity)
        }
    }
    var tasks=taskRepository.getAllTask()
    fun updateTask(taskEntity: TaskEntity){
        viewModelScope.launch {
            taskRepository.updateTask(taskEntity)
        }
    }

    fun deleteTask(taskEntity: TaskEntity){
        viewModelScope.launch {
            taskRepository.deleteTask(taskEntity)
        }
    }

    fun  search(query:String){
        Log.d("QUERY",query)
        tasks = if(query.length>2){
            taskRepository.searchTask(query)
        }else{
            taskRepository.getAllTask()
        }
    }
    fun getTaskById(id: Int): LiveData<TaskEntity> {
        return taskRepository.getTaskById(id)
    }
}