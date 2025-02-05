package com.example.demo.dto

import com.example.demo.entity.FacilityType

class FacilityTypeConverter {

    companion object {
        fun getFacilityTypeFromDisplayName(displayName: String): FacilityType {
            return when (displayName) {
                "가로등" -> FacilityType.STREET_LIGHT
                "분전반" -> FacilityType.PANEL_BOARD
                "보안등" -> FacilityType.SECURITY_LIGHT
                "등주감시기" -> FacilityType.LIGHT_WATCHDOG
                "등명기" -> FacilityType.LANTERN_LIGHT
                "기타" -> FacilityType.OTHER
                else -> throw IllegalArgumentException("Unknown display name: $displayName")
            }
        }
    }
}