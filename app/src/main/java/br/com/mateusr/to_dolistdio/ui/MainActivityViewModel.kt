package br.com.mateusr.to_dolistdio.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.mateusr.to_dolistdio.data.TaskRepository
import br.com.mateusr.to_dolistdio.database.AppDatabase
import br.com.mateusr.to_dolistdio.model.Task

class MainActivityViewModel(application : Application) : ViewModel() {

    //val taskDao = TaskDAO
    //private val taskDataSource = TaskDataSourceImplementation()
    val taskRepository : TaskRepository

    init {
        val dao = AppDatabase.getDatabase(application).taskDao()
        taskRepository = TaskRepository(dao)
    }

/*    private var _taskList = MutableLiveData<List<Task>>()
    private val taskList : LiveData<List<Task>>
    get() = taskRepository.getAllTask()*/

/*    fun init() {
        //getAllTask()
    }*/

     fun getAllTask(): LiveData<List<Task>> {
        return taskRepository.getAllTask()
    }

    class MainViewModelFactory(private val application : Application) : ViewModelProvider.Factory{

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}