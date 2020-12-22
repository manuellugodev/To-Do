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
import com.manuellugodev.to_do.databinding.NewTaskFragmentBinding
import com.manuellugodev.to_do.utils.DataResult
import com.manuellugodev.to_do.room.model.Category
import com.manuellugodev.to_do.room.model.Task
import com.manuellugodev.to_do.ui.MainViewModel
import com.manuellugodev.to_do.utils.DateInputMask
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NewTaskFragment : Fragment() {

    private lateinit var binding: NewTaskFragmentBinding
    private lateinit var adapterSpinner: ArrayAdapter<String>

    private val viewModel: MainViewModel by activityViewModels<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=NewTaskFragmentBinding.inflate(inflater,container,false)
        val view=binding.root
        return view
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

        viewModel.insertCategoryStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer{

            when (it) {
                is DataResult.Loading -> {
                    showProgress()
                }
                is DataResult.Success -> {
                    hideProgress()
                    showMessage(getString(R.string.successfulCategoryRegistration))

                }
                is DataResult.Failure -> {
                    hideProgress()
                    showMessage(getString(R.string.errorInsertTask))

                }
            }
        })
    }


    private fun setListenerButtons() {
        binding.bSave.setOnClickListener {
            val categoryTask: String = binding.spiCategory.selectedItem.toString()
            val dateTask = binding.dateNewTask.text.toString()
            val task = Task(
                title =
                binding.titleNewTask.text.toString(),
                body = binding.descNewTask.text.toString(),
                categoryTask = categoryTask,
                date = dateTask
            )
            viewModel.insertTask(task)

        }

        binding.bAddCategory.setOnClickListener {
            binding.layoutAddCategory.visibility = View.VISIBLE
        }

        binding.bAcceptCategory.setOnClickListener {
            binding.layoutAddCategory.visibility = View.GONE
            val textAddCategory = binding.editAddCategory.text.toString()
            binding.editAddCategory.setText("")

            if (checkValidAddCategory(textAddCategory)) {
                insertNewCategory(textAddCategory)
            }

        }

    }


    private fun setDateDefault() {
        val dateCurrent = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        binding.dateNewTask.setText(dateCurrent.toString())
        DateInputMask(binding.dateNewTask).listen()
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

        binding.spiCategory.adapter = adapterSpinner

    }

    private fun checkValidAddCategory(textCategory: String): Boolean {
        return !textCategory.isNullOrEmpty()
    }


    private fun showProgress() {
        binding.progressInsertTask.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progressInsertTask.visibility = View.GONE
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

}