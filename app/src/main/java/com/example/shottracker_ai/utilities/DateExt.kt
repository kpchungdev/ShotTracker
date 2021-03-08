package com.example.shottracker_ai.utilities

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

val utcZoneId = ZoneId.of("UTC")

fun ZonedDateTime.toLocalZonedDateTime() = this.withZoneSameInstant(ZoneId.systemDefault())

fun LocalDateTime.toUTCZonedDateTime() = this.atZone(utcZoneId)

private val dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM")

fun LocalDate.format() = dateFormatter.format(this)