package com.ardgahgroup.usterka.data.model.api

data class NewSubmissionModel(
    val iD_zglaszajacego: Int?,
    val opis_zgloszenia: String?,
    val forma_zgloszenia: String?,
    val iD_miejsca: Int?,
    val nazwa: String?,
    val date: String?,
    val time: String?
)