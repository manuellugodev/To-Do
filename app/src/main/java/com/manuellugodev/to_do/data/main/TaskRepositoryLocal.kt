package com.manuellugodev.to_do.data.main

import com.manuellugodev.to_do.data.repositories.TasksRepository
import com.manuellugodev.to_do.data.sources.LocalTaskDataSource
import com.manuellugodev.to_do.room.Task
import com.manuellugodev.to_do.domain.DataResult

class TaskRepositoryLocal(private val localTaskDataSource: LocalTaskDataSource) : TasksRepository {

    override suspend fun getListTasks(): DataResult<List<Task>> =
            localTaskDataSource.getListTasks()

    override suspend fun deleteTask(deleteTask: Task) =
            localTaskDataSource.deleteTask(deleteTask)

    override suspend fun insertTask(newTask: Task) =
        localTaskDataSource.insertTask(newTask)

    override suspend fun updateTask(updateTask: Task) {
        localTaskDataSource.updateTask(updateTask)
    }
}