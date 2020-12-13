package com.manuellugodev.to_do.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.manuellugodev.to_do.room.Constants

@Entity(tableName = Constants.NAME_TASK_TABLE)
data class Task(
        @PrimaryKey(autoGenerate = true) val uid: Int = 0,
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "content") val body: String,
        @ColumnInfo(name = "date") val date:String,
        @ColumnInfo(name = "realized") var realized: Boolean = false,
        @ColumnInfo(name= "category") val categoryTask:String
)