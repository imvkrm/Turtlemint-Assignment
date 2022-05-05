package com.vikram.turtlemintassignment

import android.app.Application
import com.vikram.turtlemintassignment.data.IssuesRepository


class TurtlemintApplication :Application(){
    val  issuesRepository: IssuesRepository
        get() = ServiceLocator.provideIssuesRepository(this)
}