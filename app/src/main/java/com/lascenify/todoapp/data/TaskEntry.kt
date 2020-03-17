package com.lascenify.todoapp.data

import androidx.databinding.BaseObservable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity (tableName = "task")
data class TaskEntry(
    @PrimaryKey (autoGenerate = true)
    var id: Int?,
    val description:String,
    val priority:Int,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date) :BaseObservable(){

    constructor(description: String, priority: Int, updatedAt: Date):this(null, description, priority, updatedAt)
}