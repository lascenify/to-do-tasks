package com.lascenify.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.lascenify.todoapp.data.AppDatabase
import com.lascenify.todoapp.data.TaskDao
import com.lascenify.todoapp.data.TaskEntry

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    val tasks : LiveData<List<TaskEntry>?> =
        AppDatabase.getInstance(application.applicationContext)?.taskDao()?.loadAllTasks()!!

}