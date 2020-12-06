package com.manuellugodev.to_do.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
        @PrimaryKey(autoGenerate = true) val uid: Int=0,
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "content") val body:String,
        @ColumnInfo(name="realized") var realized:Boolean=false
)