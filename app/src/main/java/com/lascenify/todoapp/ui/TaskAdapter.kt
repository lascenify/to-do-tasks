package com.lascenify.todoapp.ui

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lascenify.todoapp.R
import com.lascenify.todoapp.model.TaskEntry
import java.text.SimpleDateFormat
import java.util.*

/**
 * This TaskAdapter creates and binds ViewHolders, that hold the description and priority of a task,
 * to a RecyclerView to efficiently display data.
 */
class TaskAdapter(private val mContext: Context, listener: ItemClickListener) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder?>() {
    // Member variable to handle item clicks
    private val mItemClickListener: TaskAdapter.ItemClickListener = listener

    // Class variables for the List that holds task data and the Context
    private var mTaskEntries: List<TaskEntry>? = null

    // Date formatter
    private val dateFormat =
        SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view: View = LayoutInflater.from(mContext)
            .inflate(R.layout.task_layout, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {
        // Determine the values of the wanted data
        val taskEntry: TaskEntry = mTaskEntries!![position]
        val description: String = taskEntry.description
        val priority: Int = taskEntry.priority
        val updatedAt = dateFormat.format(taskEntry.updatedAt)

        //Set values
        holder.taskDescriptionView.text = description
        holder.updatedAtView.text = updatedAt

        // Programmatically set the text and color for the priority TextView
        val priorityString = "" + priority // converts int to String
        holder.priorityView.text = priorityString
        val priorityCircle =
            holder.priorityView.background as GradientDrawable
        // Get the appropriate background color based on the priority
        val priorityColor = getPriorityColor(priority)
        priorityCircle.setColor(priorityColor)
    }


    private fun getPriorityColor(priority: Int): Int {
        var priorityColor = 0
        when (priority) {
            1 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialRed)
            2 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialOrange)
            3 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialYellow)
            else -> {
            }
        }
        return priorityColor
    }

    override fun getItemCount(): Int = if (mTaskEntries == null) { 0 } else mTaskEntries!!.size


    fun setTasks(taskEntries: List<TaskEntry>?) {
        mTaskEntries = taskEntries
        notifyDataSetChanged()
    }

    fun getTasks (): List<TaskEntry>? = mTaskEntries

    interface ItemClickListener {
        fun onItemClickListener(itemId: Int)
    }

    // Inner class for creating ViewHolders
    inner class TaskViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        // Class variables for the task description and priority TextViews
        var taskDescriptionView: TextView = itemView.findViewById(R.id.taskDescription)
        var updatedAtView: TextView = itemView.findViewById(R.id.taskUpdatedAt)
        var priorityView: TextView = itemView.findViewById(R.id.priorityTextView)

        override fun onClick(view: View) {
            val elementId: Int = mTaskEntries!![adapterPosition].id!!
            mItemClickListener.onItemClickListener(elementId)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    companion object {
        // Constant for date format
        private const val DATE_FORMAT = "dd/MM/yyy"
    }

}