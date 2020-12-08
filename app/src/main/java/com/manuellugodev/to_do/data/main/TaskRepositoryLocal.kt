package com.manuellugodev.to_do.data.main

import com.manuellugodev.to_do.data.repositories.TasksRepository
import com.manuellugodev.to_do.data.sources.LocalTaskDataSource
import com.manuellugodev.to_do.room.Task
import com.manuellugodev.to_do.domain.DataResult
import com.manuellugodev.to_do.room.Category

class TaskRepositoryLocal(private val localTaskDataSource: LocalTaskDataSource) : TasksRepository {

    override suspend fun getListTasks(category:String): DataResult<List<Task>> =
            localTaskDataSource.getListTasks(category)

    override suspend fun deleteTask(deleteTask: Task) =
            localTaskDataSource.deleteTask(deleteTask)

    override suspend fun insertTask(newTask: Task) =
        localTaskDataSource.insertTask(newTask)

    override suspend fun updateTask(updateTask: Task) {
        localTaskDataSource.updateTask(updateTask)
    }

    override suspend fun getListCategories(): DataResult<List<Category>> =
        localTaskDataSource.getListCategories()

    override suspend fun insertCategory(newCategory: Category) {
        localTaskDataSource.insertCategory(newCategory)
    }
}