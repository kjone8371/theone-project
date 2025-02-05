package com.example.demo.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder



@Service
class GeocodingService {

    private val openCageApiKey = "af36e378310b48c785e9170c3ecc77b8"  // 발급받은 OpenCage API 키

    // 주소를 경도, 위도로 변환하는 함수
    fun getCoordinates(address: String): Pair<Double, Double>? {
        // API 호출 URL 생성
        val url = UriComponentsBuilder.fromHttpUrl("https://api.opencagedata.com/geocode/v1/json")
            .queryParam("q", address)
            .queryParam("key", openCageApiKey)
            .queryParam("countrycode", "KR")  // 대한민국을 기준으로 설정
            .toUriString()

        // RestTemplate을 사용해 API 호출
        val restTemplate = RestTemplate()
        val response = restTemplate.getForObject(url, String::class.java)

        // 응답 내용을 출력하여 확인
        println("Response: $response")

        // 응답을 JSON으로 변환
        val objectMapper = ObjectMapper()
        val jsonNode: JsonNode = objectMapper.readTree(response)

        // 응답에서 경도/위도 값 추출
        val results = jsonNode["results"]
        if (results == null || results.size() == 0) {
            println("No results found for the given address.")
            return null
        }

        val latitude = results[0]["geometry"]["lat"].asDouble()
        val longitude = results[0]["geometry"]["lng"].asDouble()

        return latitude to longitude
    }

//    // 경도, 위도를 주소로 변환
//    fun coordinatesToAddress(latitude: Double, longitude: Double): String? {
//        val url = "https://api.opencagedata.com/geocode/v1/json?q=$latitude+$longitude&key=$openCageApiKey"
//        val response = restTemplate.getForObject(url, String::class.java)
//        val jsonNode: JsonNode = objectMapper.readTree(response)
//
//        // 결과에서 주소 추출
//        val results = jsonNode["results"]
//        if (results == null || results.size() == 0) {
//            return null
//        }
//
//        return results[0]["formatted"].asText()
//    }
}