package com.manuellugodev.to_do.ui.tasks

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.manuellugodev.to_do.data.repositories.TasksRepository
import com.manuellugodev.to_do.domain.DataResult
import com.manuellugodev.to_do.room.Category
import com.manuellugodev.to_do.room.Task
import com.manuellugodev.to_do.utils.FilterDate
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class MainViewModel @ViewModelInject constructor(private val repository: TasksRepository) :
    ViewModel() {

    private val dateCurrent= SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    private val _dateFilterStatus =MutableLiveData<FilterDate>()

    private val listTaskStatus = MutableLiveData<String>()

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
        _dateFilterStatus.value=FilterDate.TODAY
        insertCategory(Category(cateId = 1, nameCategory = "GENERAL"))
        listTaskStatus.value = "GENERAL"
        listCategoryStatus.value = DataResult.Loading()

    }

    fun refreshListTask(category: String) {
        listTaskStatus.value = category
    }
    fun refreshListTaskByDate(dateMode: FilterDate){
        _dateFilterStatus.value=dateMode
        listTaskStatus.value=listTaskStatus.value
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

            }catch (e: Exception){

                _insertCategoryStatus.value=DataResult.Failure(e)
            }
        }
    }

    fun fetchListTask() = listTaskStatus.switchMap { category ->
        liveData {
            emit(DataResult.Loading())

            try {

                val result = repository.getListTasks(category)

                val resultFilter=filterListByDate(result)

                emit(resultFilter)


            } catch (e: Exception) {

                emit(DataResult.Failure(e))
                Log.e("Error", e.message.toString())

            }
        }
    }

    private fun filterListByDate(result: DataResult<List<Task>>):DataResult<List<Task>> {

        val resultFilter:List<Task>

        val dateCurrent= SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        val dateLimitWeek= sumarRestarDiasFecha(SimpleDateFormat("dd/MM/yyyy").parse(dateCurrent),7)
        val dateLimitMonth=sumarRestarDiasFecha(SimpleDateFormat("dd/MM/yyyy").parse(dateCurrent),30)

        val dateLimite=SimpleDateFormat("dd/MM/yyyy").format(dateLimitWeek)

        Log.i("Fecha" ,"Fecha Limite $dateLimite")
        if(result is DataResult.Success){

            when(_dateFilterStatus.value){

                FilterDate.TODAY -> {

                    resultFilter =
                        result.data.filter { it.date.equals(dateCurrent, true) }

                    return (DataResult.Success(resultFilter))
                }
                FilterDate.WEEK -> {

                    resultFilter=
                        result.data.filter {
                            val dateTask=SimpleDateFormat("dd/MM/yyyy").parse(it.date)
                            val dCurrent=SimpleDateFormat("dd/MM/yyyy").parse(dateCurrent)

                            dateTask<dateLimitWeek && dateTask>=dCurrent }
                    return (DataResult.Success(resultFilter))

                }

                FilterDate.MONTH -> {


                    resultFilter=
                        result.data.filter {
                            val dateTask=SimpleDateFormat("dd/MM/yyyy").parse(it.date)
                            val dCurrent=SimpleDateFormat("dd/MM/yyyy").parse(dateCurrent)

                            dateTask<dateLimitMonth && dateTask>=dCurrent }

                    return (DataResult.Success(resultFilter))
                }

            }
        }
        return result
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



    fun sumarRestarDiasFecha(fecha:Date, dias:Int):Date{

        val calendar = Calendar.getInstance();

        calendar.setTime(fecha); // Configuramos la fecha que se recibe



        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0


        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos


    }
}

class MainViewModelProvider(private val repository: TasksRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(TasksRepository::class.java).newInstance(repository)
    }
}

