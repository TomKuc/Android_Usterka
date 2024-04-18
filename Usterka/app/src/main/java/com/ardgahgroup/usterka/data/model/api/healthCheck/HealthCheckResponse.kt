package com.ardgahgroup.usterka.data.model.api.healthCheck

data class HealthCheckResponse(
    var status: String = "",
    var checks: HealthCheck? = null,
    var duration: TimeSpan? = null
)
