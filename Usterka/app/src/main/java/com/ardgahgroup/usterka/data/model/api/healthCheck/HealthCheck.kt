package com.ardgahgroup.usterka.data.model.api.healthCheck

data class HealthCheck(
    var status: String = "",
    var compotent: String = "",
    var description: String = "",
    var exception: String = "",
    var duration: TimeSpan? = null
)
