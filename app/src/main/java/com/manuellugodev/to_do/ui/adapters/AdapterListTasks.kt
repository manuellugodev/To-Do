package com.manuellugodev.to_do.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.manuellugodev.to_do.R
import com.manuellugodev.to_do.room.Task

class AdapterListTasks(private val tasks: List<Task>,private val listener:ListenerTask):RecyclerView.Adapter<AdapterListTasks.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterListTasks.ViewHolder {

        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_task,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterListTasks.ViewHolder, position: Int) {

        holder.txtTitle.text=tasks[position].title
        holder.txtDescription.text=tasks[position].body

        holder.checkTask.isChecked = tasks[position].realized

        holder.checkTask.setOnClickListener {

            val taskUpdate=tasks[position]
            val checked=holder.checkTask.isChecked

            taskUpdate.realized=checked

            listener.onUpdateTaskChecked(taskUpdate)
        }
    }

    override fun getItemCount(): Int = tasks.size

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtTitle:TextView
        val txtDescription:TextView
        val checkTask:CheckBox
        init {
            txtTitle=view.findViewById(R.id.titleTask)
            txtDescription=view.findViewById(R.id.descTask)
            checkTask=view.findViewById(R.id.checkTaskRealized)
        }
    }

    interface ListenerTask{
        fun onUpdateTaskChecked(task: Task)
    }
}