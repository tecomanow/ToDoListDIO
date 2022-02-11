package br.com.mateusr.to_dolistdio.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey (autoGenerate = true)
    val id : Int,
    @ColumnInfo (name = "title")
    val title : String,
    @ColumnInfo (name = "date")
    val date : String,
    @ColumnInfo (name = "hour")
    val hour : String,
    @ColumnInfo (name = "description")
    val description : String,
    @ColumnInfo (name = "notification")
    val notification : Int,
    @ColumnInfo(name = "complete")
    val complete : Int
)
