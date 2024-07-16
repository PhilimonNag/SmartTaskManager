package com.philimonnag.smarttaskmanager.data.local
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val desc:String,
    val priority:Int,
    val location:String,
    val dueDate:String,
    val isCompleted:Boolean
)
