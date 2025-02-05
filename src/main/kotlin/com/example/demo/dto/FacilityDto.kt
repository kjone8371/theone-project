package com.example.demo.dto

import com.example.demo.entity.FacilityStatus
import com.example.demo.entity.FacilityType

data class FacilityDto(
    val id: Long,

    val name: String,
    val type: FacilityType,
    val status: FacilityStatus?,

    val address: String,
    val latitude: Double,
    val longitude: Double,

//    val filterOne: String?,
//    val filterTwo: String?,
//    val qr: String?,

    val department: String?,
    val fixture: String?,
    val poleFormat: String?,
    val dimmer: String?,

    val imageId: String?,
    val memo: String?,

    val phoneNumber: String?,
    val escoStatus: String?,
    val powerConsumption: String?,
    val billingType: String?,
    val poleNumber: String?,

    )