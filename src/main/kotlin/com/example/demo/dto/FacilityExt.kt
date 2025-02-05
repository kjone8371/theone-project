package com.example.demo.dto

import com.example.demo.entity.Facility

fun Facility.toDto() = FacilityDto(

    id.get,

    name,
    type,
    status,

    address,
    latitude,
    longitude,

//    filter1,
//    filter2,
//    qr,

    department,
    fixture,
    poleFormat,
    dimmer,

    image?.id?.get?.toString(),
    memo,
    phoneNumber,
    escoStatus,
    powerConsumption,
    billingType,
    poleNumber

)