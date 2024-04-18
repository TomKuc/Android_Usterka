package com.ardgahgroup.usterka.data.model.api.healthCheck

data class TimeSpan(
    var ticks: Int = 0,
    var days: Int = 0,
    var hours: Int = 0,
    var milliseconds: Int = 0,
    var minutes: Int = 0,
    var seconds: Int = 0,
    var totalDays: Double = 0.0,
    var totalHours: Double = 0.0,
    var totalMilliseconds: Double = 0.0,
    var totalMinutes: Double = 0.0,
    var totalSeconds: Double = 0.0
)