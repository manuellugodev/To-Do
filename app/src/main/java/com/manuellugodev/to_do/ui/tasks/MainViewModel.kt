package com.manuellugodev.to_do.ui.tasks

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.manuellugodev.to_do.data.repositories.TasksRepository
import com.manuellugodev.to_do.domain.DataResult
import com.manuellugodev.to_do.room.Category
import com.manuellugodev.to_do.room.Task
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.Exception


class MainViewModel @ViewModelInject constructor(private val repository: TasksRepository) :
    ViewModel() {

    private val listTaskStatus = MutableLiveData<DataResult<Boolean>>()

    private val listCategoryStatus = MutableLiveData<DataResult<Boolean>>()

    private val _insertCategoryStatus = MutableLiveData<DataResult<Boolean>>()
    val insertCateforyStatus: LiveData<DataResult<Boolean>> get() = _insertCategoryStatus

    private val _deleteTaskStatus = MutableLiveData<DataResult<Boolean>>()
    val deleteTaskStatus: LiveData<DataResult<Boolean>> get() = _deleteTaskStatus

    private val _insertTaskStatus = MutableLiveData<DataResult<Boolean>>()
    val insertTaskStatus: LiveData<DataResult<Boolean>> get() = _insertTaskStatus

    private val _updateTaskStatus = MutableLiveData<DataResult<Boolean>>()
    val updateTaskStatus: LiveData<DataResult<Boolean>> get() = _updateTaskStatus

    init {
        listTaskStatus.value = DataResult.Loading()
        listCategoryStatus.value = DataResult.Loading()
    }

    fun refreshListTask() {
        listTaskStatus.value = DataResult.Loading()
    }

    fun refreshListCategory() {
        listCategoryStatus.value = DataResult.Loading()
    }

    fun updateTask(task: Task) {

        viewModelScope.launch {

            _updateTaskStatus.value = DataResult.Loading()

            try {
                val result = repository.updateTask(task)


                _updateTaskStatus.value = DataResult.Success(true)

            } catch (e: Exception) {
                e.printStackTrace()
                _updateTaskStatus.value = DataResult.Failure(e)

            }
        }

    }

    fun deleteTask(task: Task) {

        viewModelScope.launch {

            _deleteTaskStatus.value = DataResult.Loading()

            try {
                val result = repository.deleteTask(task)

                _deleteTaskStatus.value = DataResult.Success(true)

            } catch (e: Exception) {

                _deleteTaskStatus.value = DataResult.Failure(e)

            }
        }

    }

    fun insertTask(task: Task) {


        viewModelScope.launch {

            _insertTaskStatus.value = DataResult.Loading()

            try {
                val result = repository.insertTask(task)

                _insertTaskStatus.value = DataResult.Success(true)

            } catch (e: Exception) {

                _insertTaskStatus.value = DataResult.Failure(e)

            }
        }

    }

    fun insertCategory(category: Category){

        viewModelScope.launch {

            _insertCategoryStatus.value =DataResult.Loading()

            try {

                val result= repository.insertCategory(category)

                _insertCategoryStatus.value=DataResult.Success(true)

                listCategoryStatus.value=DataResult.Loading()

            }catch (e:Exception){

                _insertCategoryStatus.value=DataResult.Failure(e)
            }
        }
    }

    fun fetchListTask() = listTaskStatus.switchMap {
        liveData {
            emit(DataResult.Loading())

            try {

                val result = repository.getListTasks()


                emit(result)


            } catch (e: Exception) {

                emit(DataResult.Failure(e))
                Log.e("Error", e.message.toString())

            }
        }
    }

    fun fetchListCategory() = listCategoryStatus.switchMap {
        liveData {
            emit(DataResult.Loading())

            try {
                val result = repository.getListCategories()

                emit(result)
            } catch (e: Exception) {

                Log.e("Error Category", e.message.toString())
            }
        }
    }


}

class MainViewModelProvider(private val repository: TasksRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(TasksRepository::class.java).newInstance(repository)
    }
}

