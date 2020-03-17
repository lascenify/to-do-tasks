package com.lascenify.todoapp.bindings

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lascenify.todoapp.R
/*
@BindingAdapter("setAdapter")
fun bindRecyclerViewAdapter(
    recyclerView: RecyclerView,
    adapter: RecyclerView.Adapter<*>?
) {
    recyclerView.setHasFixedSize(true)
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.adapter = adapter
}

@BindingAdapter("bindPriorityColor")
fun bindPriorityColor(textView: TextView,
                      priority: Int): Int {
    val mContext = textView.context
    var priorityColor = 0
    when (priority) {
        1 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialRed)
        2 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialOrange)
        3 -> priorityColor = ContextCompat.getColor(mContext, R.color.materialYellow)
        else -> {
        }
    }


    return priorityColor
}*/