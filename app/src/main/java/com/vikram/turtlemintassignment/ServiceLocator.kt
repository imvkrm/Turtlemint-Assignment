package com.vikram.turtlemintassignment

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.vikram.turtlemintassignment.data.DefaultIssuesRepository
import com.vikram.turtlemintassignment.data.IssuesDataSource
import com.vikram.turtlemintassignment.data.IssuesRepository
import com.vikram.turtlemintassignment.data.local.IssuesDatabase
import com.vikram.turtlemintassignment.data.local.IssuesLocalDataSource
import com.vikram.turtlemintassignment.data.remote.IssuesApi
import com.vikram.turtlemintassignment.data.remote.IssuesRemoteDataSource
import com.vikram.turtlemintassignment.other.Constants
import com.vikram.turtlemintassignment.other.Constants.BASE_URL
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceLocator {

    private val lock = Any()
    private var database: IssuesDatabase? = null
    private var issuesApi:IssuesApi?=null

    @Volatile
    var tasksRepository: IssuesRepository? = null
        @VisibleForTesting set

    fun provideIssuesRepository(context: Context): IssuesRepository {
        synchronized(this) {
            return tasksRepository ?: createTasksRepository(context)
        }
    }

    private fun createTasksRepository(context: Context): IssuesRepository {
        val newRepo =
            DefaultIssuesRepository(createIssuesRemoteDataSource(), createTaskLocalDataSource(context))
        tasksRepository = newRepo
        return newRepo
    }


    private fun createIssuesRemoteDataSource(): IssuesDataSource {
        val issuesApi = issuesApi ?: createIssuesApi()
        return IssuesRemoteDataSource(issuesApi)
    }

    private fun createIssuesApi(): IssuesApi {
        val result = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL).build().create(IssuesApi::class.java)

        issuesApi = result
        return result
    }



    private fun createTaskLocalDataSource(context: Context): IssuesDataSource {
        val database = database ?: createDataBase(context)
        return IssuesLocalDataSource(database.issuesDao())
    }

    private fun createDataBase(context: Context): IssuesDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext, IssuesDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()

        database = result
        return result
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
//            runBlocking {
//                IssuesRemoteDataSource(issuesApi!!).deleteAllTasks()
//             //  IssuesRemoteDataSource.deleteAllTasks()
//            }
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            tasksRepository = null
        }
    }
}

