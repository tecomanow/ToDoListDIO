package br.com.mateusr.to_dolistdio.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.mateusr.to_dolistdio.ui.adapter.TaskAdapter
import br.com.mateusr.to_dolistdio.databinding.ActivityMainBinding
import br.com.mateusr.to_dolistdio.data.model.Task
import br.com.mateusr.to_dolistdio.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val mainActivityViewModel : MainActivityViewModel by viewModels {
        MainActivityViewModel.MainViewModelFactory(application)
    }

    //private val taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //mainActivityViewModel.init()

        initListener()
        initObserver()
        setEmptyVisibility(false)

    }

    private fun initObserver(){

        mainActivityViewModel.getAllTask().observe(this) {

            if(it.isNotEmpty()){
                setEmptyVisibility(false)
            }else{
                //Toast.makeText(this, "Lista não vazia: ${it.size}", Toast.LENGTH_SHORT).show()
                setEmptyVisibility(true)
            }

            populateTaskInRecyclerView(it)

        }
    }

    private fun setEmptyVisibility(visibility : Boolean) {
        if(visibility) binding.linearLayoutEmpyt.visibility = View.VISIBLE else binding.linearLayoutEmpyt.visibility = View.GONE
    }

    private fun initListener(){
        binding.fabNewTask.setOnClickListener {
            startActivity(Intent(this, AddNewTaskActivity::class.java))
        }
    }

    private fun populateTaskInRecyclerView(taskList: List<Task>) {
        binding.apply {

            val adapterTask = TaskAdapter(taskList)

            adapterTask.listenerDelete = {
                mainActivityViewModel.deleteTask(it)
            }

            adapterTask.listenerEdit = {
                val i = Intent(this@MainActivity, AddNewTaskActivity::class.java)
                i.putExtra("taskId", it.id)
                startActivity(i)
            }

            adapterTask.listenerCheckBox = { task: Task, b: Boolean ->

                val taskUpdated = Task(
                    task.id,
                    task.title,
                    task.date,
                    task.hour,
                    task.description,
                    task.notification,
                    if(b) 1 else 0
                )

                mainActivityViewModel.updateTask(taskUpdated)
            }

            recyclerViewTasks.run {
                hasFixedSize()
                layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                adapter = adapterTask
            }

        }
    }
}