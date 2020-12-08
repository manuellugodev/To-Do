package com.manuellugodev.to_do.sources

import com.manuellugodev.to_do.data.sources.LocalTaskDataSource
import com.manuellugodev.to_do.room.Task
import com.manuellugodev.to_do.room.TaskDatabase
import com.manuellugodev.to_do.domain.DataResult
import com.manuellugodev.to_do.domain.safeApiCall
import com.manuellugodev.to_do.room.Category
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

    override suspend fun getListTasks(category:String): DataResult<List<Task>> = withContext(Dispatchers.IO) {

        safeApiCall(
            call =
            { requestTasksList(category) },
            errorMessage = "Error"
        )
    }

    override suspend fun getListCategories(): DataResult<List<Category>> = withContext(Dispatchers.IO){
        safeApiCall(
            call =
            { requestCategoriesList()},
            errorMessage = "Error"
        )
    }

    override suspend fun insertCategory(newCategory: Category) {
        withContext(Dispatchers.IO){
            taskDao.insertCategory(newCategory)
        }
    }

    override suspend fun insertTask(newTask: Task) {
        withContext(Dispatchers.IO){
            taskDao.insertTask(newTask)
        }
    }

    override suspend fun deleteTask(deleteTask: Task) {
        withContext(Dispatchers.IO) {
            taskDao.deleteTask(deleteTask)
        }
    }




    private suspend fun requestCategoriesList():DataResult<List<Category>> {

        val results= taskDao.getListCategories()

        if(!results.isNullOrEmpty()){
            return DataResult.Success(results)
        }else{
            return DataResult.Failure(IOException("Error Obteniendo Lista Categorias"))
        }
    }
    private suspend fun requestTasksList(category: String): DataResult<List<Task>> {

        val results = taskDao.getListTasks(category)

        if (!results.isEmpty()) {
            return DataResult.Success(results)
        }

        return DataResult.Failure(exception = IOException("Error Obteniendo Lista"))
    }


}