package com.vikram.turtlemintassignment.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface IssuesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insertIssuesItems(issuesItem: IssuesItems)

    @Query("SELECT * FROM issues_items")
    fun observeAllIssuesItems():LiveData<List<IssuesItems>>

    @Query("SELECT * FROM issues_items WHERE issueNo = :issueNo")
    fun observeIssuesItemByIssueNo(issueNo:Int): LiveData<IssuesItems>

    @Query("SELECT * FROM issues_items")
    fun getAllIssuesItems():List<IssuesItems>

    @Query("SELECT * FROM issues_items WHERE issueNo = :issueNo")
    fun getIssuesItemByIssueNo(issueNo:Int): IssuesItems


    @Query("DELETE FROM issues_items")
    suspend fun deleteAllIssuesItems()

}