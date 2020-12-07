package com.manuellugodev.to_do.ui.newtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import com.manuellugodev.to_do.R
import com.manuellugodev.to_do.data.main.TaskRepositoryLocal
import com.manuellugodev.to_do.data.repositories.NewTaskRepository
import com.manuellugodev.to_do.data.repositories.TasksRepository
import com.manuellugodev.to_do.data.sources.LocalTaskDataSource
import com.manuellugodev.to_do.domain.DataResult
import com.manuellugodev.to_do.room.Category
import com.manuellugodev.to_do.room.Task
import com.manuellugodev.to_do.room.TaskDatabase
import com.manuellugodev.to_do.sources.TaskRoomDataSource
import com.manuellugodev.to_do.ui.tasks.MainViewModel
import com.manuellugodev.to_do.ui.tasks.MainViewModelProvider
import com.manuellugodev.to_do.utils.DateInputMask
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_list_task.*
import kotlinx.android.synthetic.main.new_task_fragment.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NewTaskFragment : Fragment() {

    private lateinit var adapterSpinner:ArrayAdapter<String>

    private val viewModel: MainViewModel by activityViewModels<MainViewModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        return inflater.inflate(R.layout.new_task_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapterSpinnerCategory()


        bSave.setOnClickListener{
            val task=Task(title =
            titleNewTask.text.toString(),body = descNewTask.text.toString())
            viewModel.insertTask(task)
            Toast.makeText(requireContext(),"Guardado",Toast.LENGTH_SHORT).show()
        }

        bAddCategory.setOnClickListener {
            layoutAddCategory.visibility=View.VISIBLE
        }

        bAcceptCategory.setOnClickListener {
            layoutAddCategory.visibility=View.GONE
            val textAddCategory=editAddCategory.text.toString()
            editAddCategory.setText("")

            insertNewCategory(textAddCategory)
        }

        val dateCurrent= SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).format(Date())

        dateNewTask.setText(dateCurrent.toString())

        Toast.makeText(requireContext(), dateCurrent.toString(), Toast.LENGTH_SHORT).show()
        DateInputMask(dateNewTask).listen()

        observerSpinner()





    }

    private fun insertNewCategory(textAddCategory: String) {

        val newCategory=Category(nameCategory = textAddCategory)
        viewModel.insertCategory(newCategory)

    }

    private fun observerSpinner() {
        viewModel.fetchListCategory().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){

                is DataResult.Loading ->{}

                is DataResult.Success->{

                    val listNameCategory:List<String> = it.data.map { it.nameCategory }

                    adapterSpinner.clear()
                    adapterSpinner.addAll(listNameCategory)
                    adapterSpinner.notifyDataSetChanged()
                }

                is DataResult.Failure->{
                    Toast.makeText(requireContext(),"Ocurrio un Error",Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setupAdapterSpinnerCategory() {

        adapterSpinner= ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item)

        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spiCategory.adapter=adapterSpinner

    }
}