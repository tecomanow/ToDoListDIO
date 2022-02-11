package br.com.mateusr.to_dolistdio.data

import br.com.mateusr.to_dolistdio.data.daos.TaskDao
import br.com.mateusr.to_dolistdio.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TaskRepository(private val dao : TaskDao) {

    fun insert(task : Task) = runBlocking {
        launch(Dispatchers.IO){
            dao.insert(task)
        }
    }

    fun delete(task: Task) = runBlocking {
        launch(Dispatchers.IO){
            dao.delete(task)
        }
    }

    fun update(task: Task) = runBlocking {
        launch(Dispatchers.IO) {
            dao.edit(task)
        }
    }

    fun getAllTask() = dao.getAll()

    fun getTaskById(id : Int) = dao.getTaskById(id)

}