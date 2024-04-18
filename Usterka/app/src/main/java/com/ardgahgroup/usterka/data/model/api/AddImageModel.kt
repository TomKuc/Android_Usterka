package com.ardgahgroup.usterka.data.model.api

data class AddImageModel(
    var fileName: String,
    var imageFile: String,
    var notificationId: Int,
    var placeId: Int
)