package com.vikram.turtlemintassignment.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [IssuesItems::class], version = 1)
abstract class IssuesDatabase : RoomDatabase() {
    abstract fun issuesDao(): IssuesDao
}