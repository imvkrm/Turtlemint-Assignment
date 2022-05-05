package com.vikram.turtlemintassignment.di
/*


import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vikram.turtlemintassignment.R
import com.vikram.turtlemintassignment.data.DefaultIssuesRepository
import com.vikram.turtlemintassignment.data.IssuesRepository
import com.vikram.turtlemintassignment.data.local.IssuesDao
import com.vikram.turtlemintassignment.data.local.IssuesDatabase
import com.vikram.turtlemintassignment.data.remote.IssuesApi
import com.vikram.turtlemintassignment.other.Constants.BASE_URL
import com.vikram.turtlemintassignment.other.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesIssuesApi(): IssuesApi =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL).build().create(IssuesApi::class.java)


    @Provides
    @Singleton
    fun providesIssuesDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, IssuesDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun providesIssuesDao(database: IssuesDatabase) = database.issuesDao()

    @Provides
    @Singleton
    fun providesGlideInstance(@ApplicationContext context: Context) =
        Glide.with(context).setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_baseline_portrait)
                .error(R.drawable.ic_baseline_portrait)
        )

    @Singleton
    @Provides
    fun providesDefaultIssuesRepository(
        dao: IssuesDao,
        api: IssuesApi
    ) = DefaultIssuesRepository(dao, api) as IssuesRepository

}*/
