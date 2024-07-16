package com.philimonnag.smarttaskmanager.data.local
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [TaskEntity::class], version = 1)
abstract class TaskDB :RoomDatabase(){
    abstract fun  taskDao():TaskDAO
    companion object{
        @Volatile
        private var INSTANCE:TaskDB?= null
        fun getInstance(ctx:Context):TaskDB{
            synchronized(this){
                var instance= INSTANCE
                if(instance==null){
                    instance=Room
                        .databaseBuilder(ctx,TaskDB::class.java,"task_db")
                        .build()
                    INSTANCE=instance
                }
                 return instance
            }
        }
    }
}