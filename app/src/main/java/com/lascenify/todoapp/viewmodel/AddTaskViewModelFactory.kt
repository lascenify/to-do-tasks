package com.lascenify.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lascenify.todoapp.data.AppDatabase

class AddTaskViewModelFactory(val mDatabase:AppDatabase, val taskId:Int) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddTaskViewModel (mDatabase, taskId) as T
    }
}