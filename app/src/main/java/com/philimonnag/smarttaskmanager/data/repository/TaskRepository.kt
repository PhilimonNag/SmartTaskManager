package com.philimonnag.smarttaskmanager.data.repository

import androidx.lifecycle.LiveData
import com.philimonnag.smarttaskmanager.data.local.TaskDAO
import com.philimonnag.smarttaskmanager.data.local.TaskDB
import com.philimonnag.smarttaskmanager.data.local.TaskEntity

class TaskRepository(private val taskDB: TaskDB) {
    suspend fun addTask(taskEntity: TaskEntity){
        taskDB.taskDao().addTask(taskEntity)
    }
    fun getAllTask()=taskDB.taskDao().getAllTask()
    suspend fun updateTask(taskEntity: TaskEntity){
        taskDB.taskDao().updateTask(taskEntity)
    }
    suspend fun deleteTask(taskEntity: TaskEntity){
        taskDB.taskDao().deleteTask(taskEntity)
    }
    fun searchTask(query:String)=taskDB.taskDao().searchBooks(query)
    fun getTaskById(id:Int):LiveData<TaskEntity>{
        return taskDB.taskDao().getTaskById(id)
    }

}