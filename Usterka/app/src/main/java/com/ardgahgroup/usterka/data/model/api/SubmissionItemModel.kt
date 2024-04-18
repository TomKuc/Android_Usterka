package com.ardgahgroup.usterka.data.model.api

data class SubmissionItemModel(
    var id: Int = 0,
    var nazwa: String? = null,
    var iD_zglaszajacego: Int? = null,
    var iD_pracownika_BHP: Int? = null,
    var iD_osoby_odpowiedzialnej: Int? = null,
    var iD_miejsca: Int = -1,
    var stan_zagrozenia: Int? = null,
    var opis_zgloszenia: String? = null,
    var przyczyny_zdarzenia_uwagi: String? = null,
    var dzial: String? = null,
    var happendDate: String? = null,
    var deadline: String? = null
)