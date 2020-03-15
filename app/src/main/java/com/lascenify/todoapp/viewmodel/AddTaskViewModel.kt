package com.lascenify.todoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lascenify.todoapp.data.AppDatabase
import com.lascenify.todoapp.data.TaskEntry

class AddTaskViewModel (private val mDatabase:AppDatabase,
                        private val taskId:Int) : ViewModel() {
    val task : LiveData<TaskEntry?> = mDatabase.taskDao()?.loadTaskById(taskId)!!
}