package com.manuellugodev.to_do.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.manuellugodev.to_do.R
import com.manuellugodev.to_do.room.model.Task

class AdapterListTasks(private var tasks: List<Task>, private val listener: ListenerTask) :
    RecyclerView.Adapter<AdapterListTasks.ViewHolder>() {

    fun updateDataAdapter(listTask: List<Task>) {
        tasks = listTask
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterListTasks.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterListTasks.ViewHolder, position: Int) {

        val task = tasks[position]

        holder.bind(task)
    }

    override fun getItemCount(): Int = tasks.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtTitle: TextView = view.findViewById(R.id.titleTask)
        private val txtDescription: TextView = view.findViewById(R.id.descTask)
        private val txtDate: TextView = view.findViewById(R.id.textDateTask)
        private val checkTask: CheckBox = view.findViewById(R.id.checkTaskRealized)

        fun bind(task: Task) {

            txtTitle.text = task.title
            txtDescription.text = task.body
            txtDate.text = task.date

            checkTask.isChecked = task.realized

            checkTask.setOnClickListener {

                task.realized = checkTask.isChecked

                listener.onUpdateTaskChecked(task)
            }

            itemView.setOnLongClickListener {

                listener.onDeleteTask(task)


                true
            }
        }
    }

    interface ListenerTask {
        fun onUpdateTaskChecked(task: Task)
        fun onDeleteTask(task: Task)
    }
}