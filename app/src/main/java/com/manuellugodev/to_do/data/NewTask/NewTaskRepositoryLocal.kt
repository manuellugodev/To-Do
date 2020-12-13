package com.manuellugodev.to_do.data.NewTask

import com.manuellugodev.to_do.data.repositories.NewTaskRepository
import com.manuellugodev.to_do.data.sources.LocalTaskDataSource
import com.manuellugodev.to_do.room.model.Task

class NewTaskRepositoryLocal(private val localTaskDataSource: LocalTaskDataSource) : NewTaskRepository {

    override suspend fun insertTask(newTask: Task) =
            localTaskDataSource.insertTask(newTask)


    override suspend fun deleteTask(deleteTask: Task) =
            localTaskDataSource.deleteTask(deleteTask)

}