package br.com.mateusr.to_dolistdio.ui

import android.app.AlarmManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.mateusr.to_dolistdio.adapter.TaskAdapter
import br.com.mateusr.to_dolistdio.databinding.ActivityMainBinding
import br.com.mateusr.to_dolistdio.helper.AlarmHelper
import br.com.mateusr.to_dolistdio.model.Task

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

        //mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        //mainActivityViewModel by viewModels
        //mainActivityViewModel.init()

        //configureRecyclerView()
        initListener()
        initObserver()
        setEmptyVisibility(false)

        /*val alarmHelper = AlarmHelper()
        alarmHelper.setAlarm(this, System.currentTimeMillis(), "TESTE NOTIFICATION")*/
    }

    private fun initObserver(){

        mainActivityViewModel.getAllTask().observe(this) {

            //Toast.makeText(this, it.size.toString(), Toast.LENGTH_SHORT).show()

            if(it.isNotEmpty()){
                populateTaskInRecyclerView(it)
                setEmptyVisibility(false)

                //Toast.makeText(this, it.size.toString(), Toast.LENGTH_SHORT).show()
                //Log.d("InsertBD", it.size.toString())
            }else{
                setEmptyVisibility(true)
            }
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
                mainActivityViewModel.taskRepository.delete(it)
                //Toast.makeText(this@MainActivity, "Delete Pressed", Toast.LENGTH_SHORT).show()
            }

            adapterTask.listenerEdit = {
                val i = Intent(this@MainActivity, AddNewTaskActivity::class.java)
                i.putExtra("taskId", it.id)
                startActivity(i)
            }

            recyclerViewTasks.run {
                hasFixedSize()
                layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                adapter = adapterTask
            }
        }
    }

    /*private fun configureRecyclerView(){
        binding.apply {

            val adapter = TaskAdapter(taskList)

            recyclerViewTasks.run {
                hasFixedSize()
                layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                adapter = TaskAdapter(taskList)
            }
        }
    }*/
}