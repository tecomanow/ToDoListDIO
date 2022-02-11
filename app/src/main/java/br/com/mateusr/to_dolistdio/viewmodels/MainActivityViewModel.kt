package br.com.mateusr.to_dolistdio.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.mateusr.to_dolistdio.data.TaskRepository
import br.com.mateusr.to_dolistdio.data.AppDatabase
import br.com.mateusr.to_dolistdio.data.model.Task

class MainActivityViewModel(application: Application) : ViewModel() {

    //val taskDao = TaskDAO
    //private val taskDataSource = TaskDataSourceImplementation()
    private val taskRepository: TaskRepository

    init {
        val dao = AppDatabase.getDatabase(application).taskDao()
        taskRepository = TaskRepository(dao)
    }


    fun getAllTask(): LiveData<List<Task>> {
        //getTasks()
        //val tasks = taskRepository.getAllTask()
        //_taskList.postValue(tasks.value)
        return taskRepository.getAllTask()
    }

    fun findTaskById(id: Int): LiveData<Task?> {
        return taskRepository.getTaskById(id)
    }

    fun insertTask(t : Task){
        taskRepository.insert(t)
    }

    fun updateTask(t : Task){
        taskRepository.update(t)
    }

    fun deleteTask(t : Task){
        taskRepository.delete(t)
    }

    class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}