package com.manuellugodev.to_do.data.repositories

import com.manuellugodev.to_do.room.Task
import com.manuellugodev.to_do.domain.DataResult

interface NewTaskRepository {

    suspend fun insertTask(newTask: Task)
    suspend fun deleteTask(deleteTask: Task)
}