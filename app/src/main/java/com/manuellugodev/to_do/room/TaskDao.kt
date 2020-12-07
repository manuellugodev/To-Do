package com.manuellugodev.to_do.room

import androidx.room.*
import com.manuellugodev.to_do.room.Constants.GET_ALL_TASK
import com.manuellugodev.to_do.domain.DataResult
import com.manuellugodev.to_do.room.Constants.GET_ALL_CATEGORIES

@Dao
interface TaskDao {


    @Query(GET_ALL_TASK)
    suspend fun getListTasks(): List<Task>

    @Query(GET_ALL_CATEGORIES)
    suspend fun getListCategories():List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(taskItem: Task)

    @Insert
    fun insertCategory(category: Category)

    @Delete
    fun deleteTask(taskItem: Task)

    @Update
    fun updateTask(taskItem: Task)

}