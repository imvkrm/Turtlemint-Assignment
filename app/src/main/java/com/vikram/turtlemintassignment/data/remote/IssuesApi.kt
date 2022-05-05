package com.vikram.turtlemintassignment.data.remote

import com.vikram.turtlemintassignment.data.remote.response.CommentApiResponse
import com.vikram.turtlemintassignment.data.remote.response.IssuesApiResponseItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IssuesApi {

    @GET("issues")
    suspend fun getAllIssuesFromApi():List<IssuesApiResponseItem>

    @GET("issues/{issueNo}/comments")
    suspend fun getAllCommentFromApi(@Path(value = "issueNo", encoded = true) issueNo:Int):List<CommentApiResponse>
}