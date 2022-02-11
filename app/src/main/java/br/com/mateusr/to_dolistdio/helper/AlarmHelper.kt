package br.com.mateusr.to_dolistdio.helper

import android.app.AlarmManager
import android.app.PendingIntent.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import br.com.mateusr.to_dolistdio.services.BootReciever
import br.com.mateusr.to_dolistdio.services.NotifierService
import java.text.SimpleDateFormat
import java.util.*

class AlarmHelper {

    private var alarmManager : AlarmManager? = null

    companion object {
        const val ACTION_BD_NOTIFICATION = "br.com.mateusr.to_dolistdio.NOTIFICATION"
    }

    fun setAlarm(context: Context, timeToNotification: Long, title: String){

        if(timeToNotification > System.currentTimeMillis()){

            Log.d("Current Time", "Tempo de notificar é maior")

            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val i = Intent(context, NotifierService::class.java)
            i.action = ACTION_BD_NOTIFICATION
            i.putExtra("taskTitle", title)

            Log.d("AlarmManagerTime", SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale("pt", "BR")).format(timeToNotification))

            val pendingIntent = getBroadcast(context, 0, i, FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
            alarmManager!!.set(AlarmManager.RTC_WAKEUP, timeToNotification, pendingIntent)

            /*val cm = ComponentName(context, BootReciever::class.java)
            val pm = context.packageManager

            pm.setComponentEnabledSetting(
                cm,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )*/
        }

    }

    private fun checkAlarm(context: Context) :Boolean {
        val i = Intent(context, NotifierService::class.java)
        i.action = ACTION_BD_NOTIFICATION

        val pendingIntent = getBroadcast(context, 0, i, FLAG_IMMUTABLE or FLAG_CANCEL_CURRENT)

        return pendingIntent != null
    }

    fun cancelAlarm(context: Context){

        if(checkAlarm(context)){
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val i = Intent(context, NotifierService::class.java)
            i.action = ACTION_BD_NOTIFICATION
            //i.putExtra("taskTitle", title)

            //Log.d("AlarmManagerTime", SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale("pt", "BR")).format(timeToNotification))

            val pendingIntent = getBroadcast(context, 0, i, FLAG_IMMUTABLE or FLAG_CANCEL_CURRENT)
            alarmManager!!.cancel(pendingIntent)
        }
    }

}