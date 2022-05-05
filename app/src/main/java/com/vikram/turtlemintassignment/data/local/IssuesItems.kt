package com.vikram.turtlemintassignment.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "issues_items")
data class IssuesItems(
    @PrimaryKey
    var id: Int,
    var name: String,
    var title: String,
    var description: String,
    var avatar: String,
    var date: String,
    var issueNo: Int,
    var commentUrl: String
)


data class CommentsItems(
    var id: Int,
    var name: String,
    var description: String,
    var avatar: String,
    var date: String,
)


