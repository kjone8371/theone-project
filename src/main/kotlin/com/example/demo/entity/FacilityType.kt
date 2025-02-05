package com.example.demo.entity

enum class FacilityType(val displayName: String) {
    STREET_LIGHT("가로등"),
    PANEL_BOARD("분전반"),
    SECURITY_LIGHT("보안등"),
    LIGHT_WATCHDOG("등주감시기"),
    LANTERN_LIGHT("등명기"),
    OTHER("기타"),
    ;

    companion object {
        fun fromDisplayName(name: String): FacilityType {
            return entries.find{it.displayName == name}
                ?: throw IllegalArgumentException("Unknown FacilityType name: $name")
        }
    }

}