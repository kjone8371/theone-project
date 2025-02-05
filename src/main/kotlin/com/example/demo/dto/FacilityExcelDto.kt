package com.example.demo.dto

import com.example.demo.entity.FacilityType

data class FacilityExcelDto(
    val name: String,
    val address: String,
    val phoneNumber: String,
    val type: FacilityType,
    val poleFormat: String,
    val fixture: String,
    val dimmer: String,
    val escoStatus: String,
    val powerConsumption: String,
    val billingType: String,
    val department: String,
    val poleNumber : String,
    val memo: String,
)