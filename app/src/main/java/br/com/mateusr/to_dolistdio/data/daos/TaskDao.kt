package br.com.mateusr.to_dolistdio.data.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.mateusr.to_dolistdio.data.model.Task

@Dao
interface TaskDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    fun insert(task : Task)

    @Delete
    fun delete(task : Task)

    @Update
    fun edit(task : Task)

    @Query("SELECT * FROM Task")
    fun getAll() : LiveData<List<Task>>

    @Query("SELECT * FROM Task WHERE id == :id")
    fun getTaskById(id : Int) : LiveData<Task?>
}