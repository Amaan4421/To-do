package com.example.to_do.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do.R
import com.example.to_do.model.Task

class TaskAdapter(private var tasks: MutableList<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.taskName)
        val taskDueDate: TextView = itemView.findViewById(R.id.taskDueDate)
        val taskTime: TextView = itemView.findViewById(R.id.taskTime)
        val taskDescription: TextView = itemView.findViewById(R.id.taskDescription)
        val delete: ImageView = itemView.findViewById(R.id.deleteTask)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.taskName
        holder.taskDueDate.text = task.dueDate
        holder.taskTime.text = task.time
        holder.taskDescription.text = task.description

        holder.delete.setOnClickListener {
            deleteTask(holder.adapterPosition, holder.itemView.context)
        }
    }

    override fun getItemCount() = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    private fun deleteTask(position: Int, context: Context) {
        tasks.removeAt(position)
        notifyItemRemoved(position)
        Toast.makeText(context, "Task deleted successfully!!!", Toast.LENGTH_SHORT).show()
    }
}
