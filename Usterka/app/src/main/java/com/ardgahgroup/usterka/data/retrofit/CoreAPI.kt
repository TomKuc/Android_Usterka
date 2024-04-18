package com.ardgahgroup.usterka.data.retrofit

import com.ardgahgroup.usterka.data.model.api.*
import com.ardgahgroup.usterka.data.model.api.healthCheck.HealthCheckResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CoreApi {
    //Account
    @POST("/Account/Login")
    fun Login(
        @Body LoginModel: LoginModel
    ): Call<String>

    @GET("/Account/GetAccountInfo")
    fun getAccountInfo(): Call<ApiUserData>

    //AnonymousSubmission
    @GET("/AnonymousSubmission/GetAnonymousSubmissions")
    fun getAnonymousSubmissions(
        @Query("StatusId") StatusId: Int
    ): Call<List<SubmissionsWithStatusModel>>

    @POST("/AnonymousSubmission/AddSubmissionAnonymously")
    fun addSubmissionAnonymously(
        @Body NewSubmissionModel: NewSubmissionModel
    ): Call<Int>

    //Health
    @GET("/Health")
    fun health(): Call<HealthCheckResponse>

    //Image
    @GET("/Image/GetImagesForNotification")
    fun getImagesForNotification(
        @Query("Id") Id: Int
    ): Call<List<ImageModel>>

    @GET("/Image/GetImageFileFromBase64")
    fun getImageFileFromBase64(
        @Query("destination") destination: String
    ): Call<String>

    @POST("/Image/AddImageFromModel")
    fun addImageFromModel(
        @Body AddImageModel: AddImageModel
    ): Call<Void>

    //Notification
    @POST("/Notification/AddNotification")
    fun addNotification(
        @Body NewSubmissionModel: NewSubmissionModel
    ): Call<Int>

    @GET("/Notification/GetNotificationsByUser")
    fun getNotifcationByUser(
        @Query("UserId") UserId: Int
    ): Call<List<SubmissionItemModel>>

    @GET("/Notification/GetNotifcationsByStatus")
    fun getNotifcationByStatus(
        @Query("StatusId") StatusId: Int
    ): Call<List<SubmissionsWithStatusModel>>

    @GET("/Notification/GetNotifcationByUserAndStatus")
    fun getNotifcationByUserAndStatus(
        @Query("UserId") UserId: Int,
        @Query("StatusId") StatusId: Int
    ): Call<List<SubmissionsWithStatusModel>>

    @GET("/Notification/GetNotificationById")
    fun getNotifcationById(
        @Query("Id") Id: Int
    ): Call<SubmissionItemModel>

    //Place
    @GET("/Place/GetPlaceById")
    fun getPlaceById(
        @Query("Id") Id: Int
    ): Call<PlaceModel>

    @GET("/Place/GetPlacesByParentID")
    fun getPlacesByParentID(
        @Query("Id") Id: Int
    ): Call<List<PlaceModel>>

    @GET("/Place/GetParentPlace")
    fun getParentPlace(
        @Query("Id") Id: Int
    ): Call<PlaceModel>
}