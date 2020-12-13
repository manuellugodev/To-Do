package com.manuellugodev.to_do.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.manuellugodev.to_do.room.Constants


@Entity(tableName = Constants.NAME_CATEGORY_TABLE)
data class Category(
    @PrimaryKey(autoGenerate = true) val cateId: Int = 0,
    @ColumnInfo(name = "Name") val nameCategory: String
)