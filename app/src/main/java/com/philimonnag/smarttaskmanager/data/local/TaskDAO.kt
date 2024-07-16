package com.philimonnag.smarttaskmanager.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDAO {

    @Insert
    suspend fun addTask(taskEntity: TaskEntity)

    @Query("SELECT * FROM TaskEntity")
    fun  getAllTask():Flow<List<TaskEntity>>

    @Update
    suspend fun updateTask(taskEntity: TaskEntity)

    @Delete
    suspend fun deleteTask(taskEntity: TaskEntity)

    @Query("SELECT * FROM TaskEntity WHERE title LIKE '%' || :query || '%' OR `desc` LIKE '%' || :query || '%'")
    fun searchBooks(query: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM TaskEntity WHERE id= :id")
    fun getTaskById(id:Int):LiveData<TaskEntity>
}