package com.manuellugodev.to_do.ui.tasks

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.manuellugodev.to_do.R
import com.manuellugodev.to_do.databinding.FragmentListTaskBinding
import com.manuellugodev.to_do.domain.DataResult
import com.manuellugodev.to_do.room.model.Category
import com.manuellugodev.to_do.room.model.Task
import com.manuellugodev.to_do.ui.adapters.AdapterListCategory
import com.manuellugodev.to_do.ui.adapters.AdapterListTasks
import com.manuellugodev.to_do.utils.FilterDate
import dagger.hilt.android.AndroidEntryPoint


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListTaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ListTaskFragment : Fragment(), AdapterListTasks.ListenerTask ,AdapterListCategory.ListenerCategory{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding:FragmentListTaskBinding


    private val adapterRv=AdapterListTasks(listOf(),this)
    private val adapterRvCategory=AdapterListCategory(listOf(),this)


    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding= FragmentListTaskBinding.inflate(inflater,container,false)
        val view=binding.root
        return view


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvListTasks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvListTasks.adapter=adapterRv

        binding.rvCategories.layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.rvCategories.adapter=adapterRvCategory



        setupObservers()

        setupAdapterSpinner()






    }

    private fun setupAdapterSpinner() {

         ArrayAdapter.createFromResource(
            requireContext(),
            R.array.date_format_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

             binding.spinnerDate.adapter=adapter
         }



       binding.spinnerDate.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
           override fun onItemSelected(
               parent: AdapterView<*>?,
               view: View?,
               position: Int,
               id: Long
           ) {
              val selectDateSpinner= binding.spinnerDate.getItemAtPosition(position).toString()


               if(selectDateSpinner.equals(FilterDate.TODAY.name,true)){
                   viewModel.refreshListTaskByDate(FilterDate.TODAY)
               }else if(selectDateSpinner.equals(FilterDate.WEEK.name,true)){
                   viewModel.refreshListTaskByDate(FilterDate.WEEK)
               }else{
                   viewModel.refreshListTaskByDate(FilterDate.MONTH)
               }


           }

           override fun onNothingSelected(parent: AdapterView<*>?) {
           }
       }

    }

    private fun setupObservers() {
        viewModel.fetchListTask().observe(viewLifecycleOwner, Observer {
            when (it) {

                is DataResult.Loading -> {
                    showProgress()
                }
                is DataResult.Success -> {
                    hideProgress()
                    adapterRv.updateDataAdapter(it.data)
                }

                is DataResult.Failure -> {
                    hideProgress()
                    adapterRv.updateDataAdapter(listOf())
                    Log.e("Error", it.exception.message.toString())
                }
            }
        })



        viewModel.updateTaskStatus.observe(viewLifecycleOwner, Observer {

            when (it) {

                is DataResult.Loading -> {
                    showProgress()

                }
                is DataResult.Success -> {
                  hideProgress()
                    Log.i("Task","Task UPDATED")
                }
                is DataResult.Failure -> {
                    hideProgress()

                    Log.e("UPDATE","Error Update Task")

                }
            }
        })

        viewModel.fetchListCategory().observe(viewLifecycleOwner, Observer {
            when(it){

                is DataResult.Loading ->{
                    showProgress()
                }

                is DataResult.Success -> {
                    hideProgress()
                    adapterRvCategory.updateDataAdapter(it.data)
                }
                is DataResult.Failure->{
                    showMessage(getString(R.string.errorListTasks))
                    Log.e("Categories","Error Cargando Categorias")
                }
            }
        })
    }

    override fun onUpdateTaskChecked(task: Task) {
        viewModel.updateTask(task)
    }

    override fun onDeleteTask(task: Task) {

        val listener:DialogInterface.OnClickListener=DialogInterface.OnClickListener{ dialog ,whinch->
            viewModel.deleteTask(task)
            viewModel.refreshListTask("GENERAL")
        }

        showAlertDialogDelete(listener)
    }

    override fun onClickCategory(selecCategory: Category) {
        viewModel.refreshListTask(selecCategory.nameCategory)
    }

    private fun showAlertDialogDelete(listener: DialogInterface.OnClickListener){

        val alertDialog=AlertDialog.Builder(requireContext())

        alertDialog.setTitle(getString(R.string.titleAlertDelete))

        alertDialog.setPositiveButton(getString(R.string.possiButtonAlertDelete),listener)

        alertDialog.setNegativeButton(getString(R.string.negaButtonAlertDelete),null)

        alertDialog.show()
    }

    private fun showProgress() {
        binding.progressListTasks.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progressListTasks.visibility = View.GONE
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListTaskFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListTaskFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}