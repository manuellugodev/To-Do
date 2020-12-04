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
import kotlinx.android.synthetic.main.new_task_fragment.*


class NewTaskFragment : Fragment() {

    private lateinit var db: TaskDatabase
    private lateinit var  source: LocalTaskDataSource
    private lateinit var repository: TasksRepository
    private val viewModel: MainViewModel by activityViewModels<MainViewModel> { MainViewModelProvider(repository) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        return inflater.inflate(R.layout.new_task_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db= TaskDatabase.getDatabase(requireContext())
        source=TaskRoomDataSource(db)
        repository=TaskRepositoryLocal(source)


        bSave.setOnClickListener{
            val task=Task(title =
            titleNewTask.text.toString(),body = descNewTask.text.toString())
            viewModel.insertTask(task)
            Toast.makeText(requireContext(),"Guardado",Toast.LENGTH_SHORT).show()
        }

    }
}