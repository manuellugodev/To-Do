package com.manuellugodev.to_do.ui.newtask

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

import com.manuellugodev.to_do.R
import com.manuellugodev.to_do.domain.DataResult
import com.manuellugodev.to_do.room.model.Category
import com.manuellugodev.to_do.room.model.Task
import com.manuellugodev.to_do.ui.tasks.MainViewModel
import com.manuellugodev.to_do.utils.DateInputMask
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.new_task_fragment.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NewTaskFragment : Fragment() {

    private lateinit var adapterSpinner: ArrayAdapter<String>

    private val viewModel: MainViewModel by activityViewModels<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.new_task_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.resetLiveData()
        setupAdapterSpinnerCategory()
        setListenerButtons()
        setDateDefault()
        observerSpinner()
        setupObserversStateViews()


    }

    private fun setupObserversStateViews() {
        viewModel.insertTaskStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            when (it) {
                is DataResult.Loading -> {
                    showProgress()
                }
                is DataResult.Success -> {
                    hideProgress()
                    showMessage(getString(R.string.successfulRegistration))
                    findNavController().navigate(R.id.action_newTaskFragment_to_listTaskFragment)

                }
                is DataResult.Failure -> {
                    hideProgress()
                    showMessage(getString(R.string.errorInsertTask))

                }
            }

        })
    }


    private fun setListenerButtons() {
        bSave.setOnClickListener {
            val categoryTask: String = spiCategory.selectedItem.toString()
            val dateTask = dateNewTask.text.toString()
            val task = Task(
                title =
                titleNewTask.text.toString(),
                body = descNewTask.text.toString(),
                categoryTask = categoryTask,
                date = dateTask
            )
            viewModel.insertTask(task)

        }

        bAddCategory.setOnClickListener {
            layoutAddCategory.visibility = View.VISIBLE
        }

        bAcceptCategory.setOnClickListener {
            layoutAddCategory.visibility = View.GONE
            val textAddCategory = editAddCategory.text.toString()
            editAddCategory.setText("")

            if (checkValidAddCategory(textAddCategory)) {
                insertNewCategory(textAddCategory)
            }

        }

    }


    private fun setDateDefault() {
        val dateCurrent = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        dateNewTask.setText(dateCurrent.toString())
        DateInputMask(dateNewTask).listen()
    }

    private fun insertNewCategory(textAddCategory: String) {

        val newCategory = Category(nameCategory = textAddCategory)
        viewModel.insertCategory(newCategory)

    }

    private fun observerSpinner() {
        viewModel.fetchListCategory().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {

                is DataResult.Loading -> {

                    Log.i("Spinner","Loading Spinner")

                }

                is DataResult.Success -> {

                    val listNameCategory: List<String> = it.data.map { it.nameCategory }

                    adapterSpinner.clear()
                    adapterSpinner.addAll(listNameCategory)
                    adapterSpinner.notifyDataSetChanged()
                }

                is DataResult.Failure -> {
                    hideProgress()
                    Log.e("Spinner", it.exception.message.toString())

                }
            }
        })
    }

    private fun setupAdapterSpinnerCategory() {

        adapterSpinner = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item)

        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spiCategory.adapter = adapterSpinner

    }

    private fun checkValidAddCategory(textCategory: String): Boolean {
        return !textCategory.isNullOrEmpty()
    }


    private fun showProgress() {
        progressInsertTask.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressInsertTask.visibility = View.GONE
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

}