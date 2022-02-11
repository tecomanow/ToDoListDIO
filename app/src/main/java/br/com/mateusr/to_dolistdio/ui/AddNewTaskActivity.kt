package br.com.mateusr.to_dolistdio.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import br.com.mateusr.to_dolistdio.databinding.ActivityAddNewTaskBinding
import br.com.mateusr.to_dolistdio.helper.AlarmHelper
import br.com.mateusr.to_dolistdio.data.model.Task
import br.com.mateusr.to_dolistdio.viewmodels.MainActivityViewModel
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

    private var idTask = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddNewTaskBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarNewTask)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        idTask = intent.getIntExtra("taskId", 0)
        if(idTask > 0){
            supportActionBar?.title = "Editar tarefa"
            binding.btnConfirmTask.text = "Atualizar"
        }
        //Toast.makeText(this, "TaskId : $idTask", Toast.LENGTH_SHORT).show()

        configureViews(idTask)
        initListener()
    }

    private fun configureViews(idTask: Int) {

        mainActivityViewModel.findTaskById(idTask).observe(this) {
            it?.let {
            binding.apply {
                editTextTitle.setText(it.title)
                editTextDate.setText(it.date)
                editTextHour.setText(it.hour)
                edtiTextDescription.setText(it.description)
                checkBoxNotificationTask.isChecked = it.notification == 1
                notification = it.notification
            }
        }
        }
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

                if(idTask > 0){

                    val task = Task(
                        idTask,
                        binding.editTextTitle.text.toString(),
                        binding.editTextDate.text.toString(),
                        binding.editTextHour.text.toString(),
                        binding.edtiTextDescription.text.toString(),
                        notification,
                        0
                    )

                    updateTask(task)

                }else {

                    val task = Task(
                        0,
                        binding.editTextTitle.text.toString(),
                        binding.editTextDate.text.toString(),
                        binding.editTextHour.text.toString(),
                        binding.edtiTextDescription.text.toString(),
                        notification,
                        0
                    )

                    saveTask(task)
                }

            }
        }

        binding.checkBoxNotificationTask.setOnCheckedChangeListener { compoundButton, b ->
            notification = if(b) 1 else 0
        }

        binding.editTextDate.setOnClickListener {
            createDatePicker()
        }

        binding.editTextHour.setOnClickListener {
            createTimerPicker()
        }
    }

    private fun createTimerPicker() {
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

    private fun createDatePicker() {
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

    private fun updateTask(task: Task) {
        try {
            mainActivityViewModel.updateTask(task)
            if(notification == 1){
                createNotification(task)
            }else{
                val alarmHelper = AlarmHelper()
                alarmHelper.cancelAlarm(this)
            }
            finish()
        } catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun saveTask(t: Task) {

        try {
            mainActivityViewModel.insertTask(t)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == android.R.id.home){
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}