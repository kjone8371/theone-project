package com.example.demo.controller

import com.example.demo.service.GeocodingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/kakao")
class GeocodingController(private val geocodingService: GeocodingService) {

    // 주소 -> 경도, 위도 변환 API
    @GetMapping("/address-to-kakao")
    fun addressToCoordinates(@RequestParam address: String): Map<String, Double?> {
        val (latitude, longitude) = geocodingService.getCoordinates(address) ?: return mapOf("error" to null)
        return mapOf("latitude" to latitude, "longitude" to longitude)
    }



//    // 경도, 위도 -> 주소 변환 API
//    @GetMapping("/coordinates-to-address")
//    fun coordinatesToAddress(@RequestParam latitude: Double, @RequestParam longitude: Double): Map<String, String?> {
//        val address = geocodingService.coordinatesToAddress(latitude, longitude) ?: return mapOf("error" to "No address found")
//        return mapOf("address" to address)
//    }
}