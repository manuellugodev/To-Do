package com.manuellugodev.to_do.room

import androidx.room.*
import com.manuellugodev.to_do.room.Constants.GET_LIST_TASKS_BY_CATEGORY
import com.manuellugodev.to_do.room.Constants.GET_ALL_CATEGORIES

@Dao
interface TaskDao {


    @Query(GET_LIST_TASKS_BY_CATEGORY)
    suspend fun getListTasks(category:String): List<Task>

    @Query(GET_ALL_CATEGORIES)
    suspend fun getListCategories():List<Category>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertTask(taskItem: Task)

    @Insert
    fun insertCategory(category: Category)

    @Delete
    fun deleteTask(taskItem: Task)

    @Update
    fun updateTask(taskItem: Task)

}