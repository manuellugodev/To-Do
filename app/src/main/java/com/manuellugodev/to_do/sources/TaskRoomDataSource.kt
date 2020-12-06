package com.manuellugodev.to_do.sources

import com.manuellugodev.to_do.data.sources.LocalTaskDataSource
import com.manuellugodev.to_do.room.Task
import com.manuellugodev.to_do.room.TaskDatabase
import com.manuellugodev.to_do.domain.DataResult
import com.manuellugodev.to_do.domain.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class TaskRoomDataSource(db: TaskDatabase) : LocalTaskDataSource {

    private val taskDao = db.taskDao()

    override suspend fun updateTask(updateTask: Task) {
        withContext(Dispatchers.IO){
            taskDao.updateTask(updateTask)
        }
    }

    override suspend fun getListTasks(): DataResult<List<Task>> = withContext(Dispatchers.IO) {

        safeApiCall(
            call =
            { requestTasksList() },
            errorMessage = "Error"
        )
    }


    override suspend fun insertTask(newTask: Task) {
        withContext(Dispatchers.IO){
            taskDao.insertTask(newTask)
        }
    }

    override suspend fun deleteTask(deleteTask: Task) {
        taskDao.deleteTask(deleteTask)
    }


    private suspend fun requestTasksList(): DataResult<List<Task>> {

        val results = taskDao.getListTasks()

        if (!results.isEmpty()) {
            return DataResult.Success(results)
        }

        return DataResult.Failure(exception = IOException("Error Obteniendo Lista"))
    }


}