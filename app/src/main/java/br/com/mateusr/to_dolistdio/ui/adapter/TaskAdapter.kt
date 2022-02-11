package br.com.mateusr.to_dolistdio.ui.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import br.com.mateusr.to_dolistdio.R
import br.com.mateusr.to_dolistdio.databinding.AdapterTaskBinding
import br.com.mateusr.to_dolistdio.data.model.Task

class TaskAdapter(
    private val tasks : List<Task>
) : RecyclerView.Adapter<TaskAdapter.MyTaskViewHolder>() {

    var listenerEdit : (t : Task) -> Unit = {}
    var listenerDelete : (t : Task) -> Unit = {}
    var listenerCheckBox : (t : Task, checked : Boolean) -> Unit = { task: Task, b: Boolean -> }

    inner class MyTaskViewHolder(private val binding: AdapterTaskBinding) : RecyclerView.ViewHolder(binding.root){

        /*val tvTitle : TextView = itemView.findViewById(R.id.textViewAdapterTitle)
        val tvTime : TextView = itemView.findViewById(R.id.textViewAdapterTime)
        val imgBtnShowPopUp : ImageButton = itemView.findViewById(R.id.imageButtonShowPopUp)*/

        fun bind(task : Task){

            binding.apply {
                textViewAdapterTitle.text = task.title
                textViewAdapterTime.text = "${task.date} às ${task.hour}"
                textViewAdapterDescription.text = task.description

                textViewAdapterDescription.visibility  = if (task.description == "") View.GONE else View.VISIBLE

                imageButtonShowPopUp.setOnClickListener {
                    showPopUpMenu(task)
                }

                if(task.complete == 1){
                    textViewAdapterTitle.paintFlags = (textViewAdapterTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                    textViewAdapterTime.paintFlags = (textViewAdapterTime.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                    checkBoxCompleted.isChecked = true
                }

                checkBoxCompleted.setOnCheckedChangeListener { compoundButton, b ->
                    listenerCheckBox(task, b)
                }


            }
        }

        private fun showPopUpMenu(task: Task) {
            val popUpMenu = PopupMenu(binding.imageButtonShowPopUp.context, binding.imageButtonShowPopUp)
            popUpMenu.menuInflater.inflate(R.menu.task_menu, popUpMenu.menu)
            popUpMenu.setOnMenuItemClickListener {

                when (it.itemId){

                    R.id.actionMenuEdit -> {
                        listenerEdit(task)
                    }

                    R.id.actionMenuDelete -> {
                        listenerDelete(task)
                    }

                }

                return@setOnMenuItemClickListener true
            }
            popUpMenu.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterTaskBinding.inflate(inflater, parent, false)
        return MyTaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyTaskViewHolder, position: Int) {

        holder.bind(tasks[position])
    /*val t = tasks[position]

        holder.apply {
            tvTitle.text = t.title
            tvTime.text = "${t.date} às ${t.hour}"

            imgBtnShowPopUp.setOnClickListener {

            }
        }*/
    }

    override fun getItemCount(): Int = tasks.size

}