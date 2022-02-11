package br.com.mateusr.to_dolistdio.services

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import br.com.mateusr.to_dolistdio.data.TaskRepository
import br.com.mateusr.to_dolistdio.data.AppDatabase
import kotlinx.coroutines.runBlocking

class BootReciever : BroadcastReceiver() {

    companion object {
        const val BOOT_ACTION = "android.intent.action.BOOT_COMPLETED"
    }

    override fun onReceive(p0: Context?, p1: Intent?) {

        if(p1 != null && p1.action != null){

            if (p1.action == BOOT_ACTION){
                val dao = AppDatabase.getDatabase((p0 as Application).applicationContext).taskDao()
                val taskRepository = TaskRepository(dao)

                runBlocking {
                    val tarefas = taskRepository.getAllTask()
                    tarefas.value?.forEach { task ->
                        Toast.makeText(p0, task.title, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}