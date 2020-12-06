package com.manuellugodev.to_do.data.sources

import com.manuellugodev.to_do.room.Task
import com.manuellugodev.to_do.domain.DataResult

interface LocalTaskDataSource {

    suspend fun getListTasks(): DataResult<List<Task>>

    suspend fun insertTask(newTask: Task)

    suspend fun deleteTask(deleteTask: Task)

    suspend fun updateTask(updateTask: Task)


}