package com.lascenify.todoapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lascenify.todoapp.MainActivity
import com.lascenify.todoapp.R
import com.lascenify.todoapp.data.AppDatabase
import com.lascenify.todoapp.net.AppExecutors
import kotlinx.android.synthetic.main.task_list_fragment.*

class TaskListFragment :Fragment(), TaskAdapter.ItemClickListener{

    private lateinit var mDatabase :AppDatabase
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mAdapter: TaskAdapter

    companion object{
        private val TAG: String = MainActivity::class.java.simpleName
        private val TASK_LOADER_ID = 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.task_list_fragment, container, false)
        mRecyclerView = rootView.findViewById(R.id.recyclerViewTasks)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDatabase = AppDatabase.getInstance(context!!)!!

        mAdapter = TaskAdapter(context!!, this)
        mRecyclerView.adapter = mAdapter

        val decoration = DividerItemDecoration(context, VERTICAL)
        mRecyclerView.addItemDecoration(decoration)
        setListeners()

    }

    private fun setListeners(){
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                AppExecutors.instance?.diskIO()?.execute {
                    val position = viewHolder.adapterPosition
                    val task = mAdapter.getTasks()?.get(position)!!
                    mDatabase.taskDao()?.deleteTask(task)
                    retrieveTasks()
                }
            }
        }).attachToRecyclerView(recyclerViewTasks)

        view?.findViewById<FloatingActionButton>(R.id.fab)?.setOnClickListener {
            findNavController().navigate(R.id.addTaskFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        AppExecutors.instance?.diskIO()!!.execute { retrieveTasks() }
    }

    private fun retrieveTasks() {
        val tasks = mDatabase.taskDao()?.loadAllTasks()!!
        activity?.runOnUiThread { mAdapter.setTasks(tasks) }
    }

    override fun onItemClickListener(itemId: Int) {
        findNavController().navigate(R.id.addTaskFragment)
    }
}