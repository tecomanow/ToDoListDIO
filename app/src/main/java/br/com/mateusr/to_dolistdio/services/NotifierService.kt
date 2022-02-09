package br.com.mateusr.to_dolistdio.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import br.com.mateusr.to_dolistdio.helper.NotificationHelper

class NotifierService : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {

        val title = "Você precisa: ${p1?.getStringExtra("taskTitle")}"
        val message = "Você possui uma tarefa para realizar"

        Log.d("NOTIFICATION", message)

        val notificationHelper = NotificationHelper(p0!!)
        val nBuilder = notificationHelper.getNotification(title ?: "Tarefa para realizar", message)
        notificationHelper.notify(1, nBuilder)

    }
}