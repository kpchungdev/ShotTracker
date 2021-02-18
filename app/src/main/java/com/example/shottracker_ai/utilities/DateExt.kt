package com.example.shottracker_ai.utilities

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

val utcZoneId = ZoneId.of("UTC")

fun ZonedDateTime.toLocalZonedDateTime() = this.withZoneSameInstant(ZoneId.systemDefault())

fun LocalDateTime.toUTCZonedDateTime() = this.atZone(utcZoneId)
