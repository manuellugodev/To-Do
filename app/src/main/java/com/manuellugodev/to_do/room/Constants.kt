package com.manuellugodev.to_do.room

object Constants {

    //Table Name
    const val NAME_TASK_TABLE="Tasks"
    const val NAME_CATEGORY_TABLE="Categories"

    const val NAME_DATABASE="DB_Tasks"
    const val GET_LIST_TASKS_BY_CATEGORY="SELECT * FROM $NAME_TASK_TABLE WHERE category LIKE :category"
    const val GET_ALL_CATEGORIES="SELECT * FROM $NAME_CATEGORY_TABLE"

}