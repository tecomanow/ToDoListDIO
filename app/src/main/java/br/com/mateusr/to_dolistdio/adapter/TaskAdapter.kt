package br.com.mateusr.to_dolistdio.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.mateusr.to_dolistdio.R
import br.com.mateusr.to_dolistdio.model.Task

class TaskAdapter(
    private val tasks : List<Task>
) : RecyclerView.Adapter<TaskAdapter.MyTaskViewHolder>() {

    inner class MyTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTitle : TextView = itemView.findViewById(R.id.textViewAdapterTitle)
        val tvTime : TextView = itemView.findViewById(R.id.textViewAdapterTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTaskViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_task, parent, false)
        return MyTaskViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyTaskViewHolder, position: Int) {
        val t = tasks[position]

        holder.apply {
            tvTitle.text = t.title
            tvTime.text = "${t.date} às ${t.hour}"
        }
    }

    override fun getItemCount(): Int = tasks.size

}