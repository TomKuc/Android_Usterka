package com.ardgahgroup.usterka.data.model.api

data class SubmissionsWithStatusModel(
    var id: Int = 0,
    var name: String? = null,
    var bhpId: Int = 0,
    var resposibleId: Int = 0,
    var placeId: Int = 0,
    var dangerLevel: Int = 0,
    var description: String? = null,
    var comment: String? = null,
    var deadline: String? = null,
    var statusTime: String? = null,
    var statusId: Int = 0,
    var statusDescription: String? = null,
    var dzial: String? = null,
    var happendDate: String? = null,
    var iD_zglaszajacego: Int = 0
)
