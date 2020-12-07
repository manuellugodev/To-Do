package com.manuellugodev.to_do.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Task::class,Category::class),version = 1)
abstract class TaskDatabase:RoomDatabase() {

   abstract fun taskDao():TaskDao

   companion object{
      private var INSTANCE:TaskDatabase?=null
      private const val DB_NAME="DB_Tasks"

      fun getDatabase(context: Context):TaskDatabase{

         if(INSTANCE==null){
            synchronized(TaskDatabase::class.java){
               if(INSTANCE==null){
                  INSTANCE=Room.databaseBuilder(context.applicationContext,TaskDatabase::class.java,
                     DB_NAME).build()
               }
            }
         }
         return INSTANCE!!
      }
   }
}