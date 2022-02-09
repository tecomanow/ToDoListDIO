package br.com.mateusr.to_dolistdio.helper

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.util.Log
import br.com.mateusr.to_dolistdio.services.NotifierService
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AlarmHelper {

    private var alarmManager : AlarmManager? = null

    companion object {
        const val ACTION_BD_NOTIFICATION = "br.com.mateusr.to_dolistdio.NOTIFICATION"
    }

    fun setAlarm(context: Context, timeToNotification: Long, title: String){

        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(context, NotifierService::class.java)
        i.action = ACTION_BD_NOTIFICATION
        i.putExtra("taskTitle", title)

        Log.d("AlarmManagerTime", SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale("pt", "BR")).format(timeToNotification))

        val pendingIntent = getBroadcast(context, 0, i, FLAG_IMMUTABLE or FLAG_CANCEL_CURRENT)
        alarmManager!!.set(AlarmManager.RTC_WAKEUP, timeToNotification, pendingIntent)

    }

}