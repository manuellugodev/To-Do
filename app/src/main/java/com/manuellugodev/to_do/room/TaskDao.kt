package com.manuellugodev.to_do.room

import androidx.room.*
import com.manuellugodev.to_do.room.Constants.GET_ALL_TASK
import com.manuellugodev.to_do.domain.DataResult

@Dao
interface TaskDao {


    @Query(GET_ALL_TASK)
    suspend fun getListTasks(): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(taskItem: Task)

    @Delete
    fun deleteTask(taskItem: Task)

    @Update
    fun updateTask(taskItem: Task)

}