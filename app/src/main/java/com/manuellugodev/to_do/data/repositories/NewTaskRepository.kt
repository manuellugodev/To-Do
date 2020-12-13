package com.manuellugodev.to_do.data.repositories

import com.manuellugodev.to_do.room.model.Task

interface NewTaskRepository {

    suspend fun insertTask(newTask: Task)
    suspend fun deleteTask(deleteTask: Task)
}