package com.manuellugodev.to_do.ui.newtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import com.manuellugodev.to_do.R
import com.manuellugodev.to_do.data.main.TaskRepositoryLocal
import com.manuellugodev.to_do.data.repositories.NewTaskRepository
import com.manuellugodev.to_do.data.repositories.TasksRepository
import com.manuellugodev.to_do.data.sources.LocalTaskDataSource
import com.manuellugodev.to_do.room.Task
import com.manuellugodev.to_do.room.TaskDatabase
import com.manuellugodev.to_do.sources.TaskRoomDataSource
import com.manuellugodev.to_do.ui.tasks.MainViewModel
import com.manuellugodev.to_do.ui.tasks.MainViewModelProvider
import com.manuellugodev.to_do.utils.DateInputMask
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.new_task_fragment.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NewTaskFragment : Fragment() {


    private val viewModel: MainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        return inflater.inflate(R.layout.new_task_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bSave.setOnClickListener{
            val task=Task(title =
            titleNewTask.text.toString(),body = descNewTask.text.toString())
            viewModel.insertTask(task)
            Toast.makeText(requireContext(),"Guardado",Toast.LENGTH_SHORT).show()
        }

        val dateCurrent= SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).format(Date())

        dateNewTask.setText(dateCurrent.toString())

        Toast.makeText(requireContext(), dateCurrent.toString(), Toast.LENGTH_SHORT).show()
        DateInputMask(dateNewTask).listen()

    }
}