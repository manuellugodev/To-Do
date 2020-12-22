package com.manuellugodev.to_do.data.repositories

import com.manuellugodev.to_do.room.model.Task
import com.manuellugodev.to_do.utils.DataResult
import com.manuellugodev.to_do.room.model.Category

interface TasksRepository {

    suspend fun getListTasks(category:String): DataResult<List<Task>>

    suspend fun getListCategories(): DataResult<List<Category>>

    suspend fun insertTask(newTask: Task)

    suspend fun deleteTask(deleteTask: Task)

    suspend fun updateTask(updateTask: Task)

    suspend fun insertCategory(newCategory: Category)


}