package com.manuellugodev.to_do.di.modules

import android.content.Context
import androidx.room.Room
import com.manuellugodev.to_do.data.main.TaskRepositoryLocal
import com.manuellugodev.to_do.data.repositories.TasksRepository
import com.manuellugodev.to_do.data.sources.LocalTaskDataSource
import com.manuellugodev.to_do.room.Constants
import com.manuellugodev.to_do.room.TaskDatabase
import com.manuellugodev.to_do.sources.TaskRoomDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providerDbRoomTask(@ApplicationContext contextApp: Context) =
        Room.databaseBuilder(
            contextApp, TaskDatabase::class.java,
            Constants.NAME_DATABASE
        ).build()


    @Singleton
    @Provides
    fun providesTaskSource(db: TaskDatabase):LocalTaskDataSource =
        TaskRoomDataSource(db)


    @Singleton
    @Provides
    fun provideRepositoryTasks(source: LocalTaskDataSource):TasksRepository =
        TaskRepositoryLocal(source)

}