package com.lascenify.todoapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lascenify.todoapp.R
import com.lascenify.todoapp.R.string.EXTRA_TASK_ID
import com.lascenify.todoapp.data.AppDatabase
import com.lascenify.todoapp.data.TaskEntry
import com.lascenify.todoapp.net.AppExecutors
import kotlinx.android.synthetic.main.add_task_fragment.*
import java.util.*

class AddTaskFragment :Fragment(){
    private var mPriority = 1

    // Constants for priority
    private val PRIORITY_HIGH = 1
    private val PRIORITY_MEDIUM = 2
    private val PRIORITY_LOW = 3

    // Constant for default task id to be used when not in update mode
    private val DEFAULT_TASK_ID = -1

    // Constant for logging
    private val TAG: String = AddTaskFragment::class.java.simpleName
    private var mTaskId: Int = DEFAULT_TASK_ID

    lateinit var mDatabase:AppDatabase
    lateinit var mButton: Button
    lateinit var mEditText: EditText
    lateinit var mRadioGroup: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.add_task_fragment, container, false)
        mButton = rootView.findViewById(R.id.addButton)
        mEditText = rootView.findViewById(R.id.editTextTaskDescription)
        mRadioGroup = rootView.findViewById(R.id.radioGroup)

        (rootView.findViewById<RadioButton>(R.id.radButton1)).isChecked = true
        mDatabase = AppDatabase.getInstance(context!!)!!
        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.INSTANCE_TASK_ID))) {
            mTaskId = savedInstanceState.getInt(getString(R.string.INSTANCE_TASK_ID), DEFAULT_TASK_ID)
        }

        val taskIdFromTaskList = arguments?.getInt(getString(EXTRA_TASK_ID))
        if (arguments != null && taskIdFromTaskList != null){
            mButton.text = "Update"
            if (mTaskId == DEFAULT_TASK_ID){
                mTaskId = taskIdFromTaskList
                AppExecutors.instance?.diskIO()?.execute {
                    val task = mDatabase.taskDao()?.loadTaskById(mTaskId)
                    activity?.runOnUiThread{populateUI(task)}
                }
            }
        }

        setListeners()
        return rootView
    }

    private fun populateUI(task: TaskEntry?) {
        if (task == null)
            return
        mEditText.setText(task.description)
        setPriorityInViews(task.priority)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(getString(R.string.INSTANCE_TASK_ID), mTaskId)
        super.onSaveInstanceState(outState)
    }

    private fun setListeners(){
        mButton.setOnClickListener{onSaveButtonClicked()}
    }

    private fun onSaveButtonClicked(){
        val description = editTextTaskDescription.text.toString()
        val priority = getPriorityFromViews()
        val date = Date()
        val task = TaskEntry(description, priority, date)

        AppExecutors.instance?.diskIO()!!.execute {
            if (mTaskId == DEFAULT_TASK_ID){
                mDatabase.taskDao()?.insertTask(task)
            }
            else{
                task.id = mTaskId
                mDatabase.taskDao()?.updateTask(task)
            }
            findNavController().popBackStack()
        }

    }

    private fun getPriorityFromViews(): Int {
        var priority = 1
        val checkedId = (view?.findViewById(R.id.radioGroup) as RadioGroup).checkedRadioButtonId
        when (checkedId) {
            R.id.radButton1 -> priority = PRIORITY_HIGH
            R.id.radButton2 -> priority =
                PRIORITY_MEDIUM
            R.id.radButton3 -> priority = PRIORITY_LOW
        }
        return priority
    }


    private fun setPriorityInViews(priority: Int) {
        when (priority) {
            PRIORITY_HIGH -> (view?.findViewById(R.id.radioGroup) as RadioGroup).check(
                R.id.radButton1
            )
            PRIORITY_MEDIUM -> (view?.findViewById(R.id.radioGroup) as RadioGroup).check(
                R.id.radButton2
            )
            PRIORITY_LOW -> (view?.findViewById(R.id.radioGroup) as RadioGroup).check(
                R.id.radButton3
            )
        }
    }
}