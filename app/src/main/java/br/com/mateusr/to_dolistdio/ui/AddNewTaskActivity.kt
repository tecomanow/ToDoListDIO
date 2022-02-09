package br.com.mateusr.to_dolistdio.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import br.com.mateusr.to_dolistdio.databinding.ActivityAddNewTaskBinding
import br.com.mateusr.to_dolistdio.helper.AlarmHelper
import br.com.mateusr.to_dolistdio.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AddNewTaskActivity : AppCompatActivity() {

    private val mainActivityViewModel : MainActivityViewModel by viewModels {
        MainActivityViewModel.MainViewModelFactory(application)
    }

    private lateinit var binding : ActivityAddNewTaskBinding
    private var notification = 0

    private var dateToNotification : Long = 0
    private var hourToNotification : Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddNewTaskBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarNewTask)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initListener()
    }

    private fun validateInputs() : Boolean{
        val title = binding.editTextTitle.text.toString()
        val date = binding.editTextDate.text.toString()
        val hour = binding.editTextHour.text.toString()

        if(title.isNotBlank()){
            if (date.isNotBlank()){
                if(hour.isNotBlank()){
                    return true
                }else{
                    binding.editTextHour.error = "Insira um horário"
                    return false
                }
            }else{
                binding.editTextDate.error = "Insira uma data"
                return false
            }
        }else{
            binding.editTextTitle.error = "Insira um título"
            return false
        }
    }

    private fun initListener(){

        binding.btnCancelNewTask.setOnClickListener {
            finish()
        }

        binding.btnConfirmTask.setOnClickListener {
            if(validateInputs()){

                val task = Task(
                    0,
                    binding.editTextTitle.text.toString(),
                    binding.editTextDate.text.toString(),
                    binding.editTextHour.text.toString(),
                    binding.edtiTextDescription.text.toString(),
                    notification
                )

                saveTask(task)
            }
        }

        binding.checkBoxNotificationTask.setOnCheckedChangeListener { compoundButton, b ->
            notification = if(b) 1 else 0
        }

        binding.editTextDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1

                val date = Date(it + offset)
                val dateFormated = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(date)
                binding.editTextDate.setText(dateFormated)

                dateToNotification = it
            }
            datePicker.show(supportFragmentManager, "datePicker")
        }

        binding.editTextHour.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

                timePicker.addOnPositiveButtonClickListener {
                    val time = String.format(Locale("pt", "BR"),"%02d:%02d", timePicker.hour, timePicker.minute)
                    binding.editTextHour.setText(time)

                    val c = Calendar.getInstance()
                    //c.timeInMillis = dateToNotification
                    c.set(Calendar.HOUR, timePicker.hour)
                    c.set(Calendar.MINUTE, timePicker.minute)

                    hourToNotification = c.timeInMillis
                }
                timePicker.show(supportFragmentManager, "timePicker")

        }
    }

    private fun saveTask(t: Task) {

        try {
            mainActivityViewModel.taskRepository.insert(t)
            if(notification == 1){
                createNotification(t)
            }
            finish()
        }catch (e : Exception){
            e.printStackTrace()
        }

//        val taskDAO = BackupTaskDAO(this)
//        if(taskDAO.insert(t)){
//            if(notification == 1){
//                createNotification(t)
//            }
//            finish()
//        } else {
//            Toast.makeText(this, "Algo deu errado", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun createNotification(t: Task) {
        val cHour = Calendar.getInstance()
        cHour.timeInMillis = hourToNotification

        val c = Calendar.getInstance()
        c.timeInMillis = dateToNotification
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)+1)
        c.set(Calendar.HOUR, cHour.get(Calendar.HOUR))
        c.set(Calendar.MINUTE, cHour.get(Calendar.MINUTE))

        val alarmHelper = AlarmHelper()
        alarmHelper.setAlarm(this, c.timeInMillis, t.title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == android.R.id.home){
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}