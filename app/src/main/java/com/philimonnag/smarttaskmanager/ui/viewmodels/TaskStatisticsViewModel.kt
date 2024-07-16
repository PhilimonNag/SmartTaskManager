package com.philimonnag.smarttaskmanager.ui.viewmodels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.philimonnag.smarttaskmanager.data.local.TaskEntity
import com.philimonnag.smarttaskmanager.data.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TaskStatisticsViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    private val _taskStats = MutableLiveData<TaskStats>()
    val taskStats: LiveData<TaskStats> get() = _taskStats

    init {
        viewModelScope.launch {
            // Load data from the repository
            calculateTaskStats()
        }
    }

    suspend fun calculateTaskStats() {
        // Collect Flow from repository and process tasks
        taskRepository.getAllTask().collect { tasks ->
            val completedTasks = tasks.count { it.isCompleted }
            val pendingTasks = tasks.size - completedTasks
            val priorityDistribution = tasks.groupBy { it.priority }
                .mapValues { it.value.size }

            // Update LiveData with new TaskStats
            _taskStats.value = TaskStats(completedTasks, pendingTasks, priorityDistribution)
        }
    }
}

data class TaskStats(
    val completedTasks: Int,
    val pendingTasks: Int,
    val priorityDistribution: Map<Int, Int> // Priority as key, count as value
)