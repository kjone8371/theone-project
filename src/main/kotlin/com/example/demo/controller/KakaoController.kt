package com.example.demo.controller

import com.example.demo.service.KakaoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class KakaoController(private val kakaoService: KakaoService) {

    // 주소를 경도와 위도로 변환하는 엔드포인트
    @GetMapping("/getCoordinates")
    fun getCoordinates(@RequestParam("address") address: String): Map<String, Any> {
        val jsonResponse = kakaoService.getKakaoApiFromAddress(address)

        return if (jsonResponse != null) {
            val coordinates = kakaoService.changeToJSON(jsonResponse)
            mapOf("latitude" to coordinates[1], "longitude" to coordinates[0])
        } else {
            mapOf("error" to "주소를 찾을 수 없습니다.")
        }
    }


}