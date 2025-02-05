package com.example.demo.service


import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection
import java.net.URLEncoder
import java.util.ArrayList


@Service
class KakaoService {

    @Value("\${KAKAO_API_KEY}")
    private lateinit var apiKey: String

    @Value("\${KAKAO_URL}")
    private lateinit var apiUrl: String

    // 주소를 받아 카카오 API로부터 경도와 위도를 가져오는 함수
    fun getKakaoApiFromAddress(fullAddr: String): String? {

        if (fullAddr.isNullOrEmpty()) {
            println("Error: Address is null or empty")
            return null // 주소가 비어 있으면 null 반환
        }

        var jsonString: String? = null

        try {
            val encodedAddr = URLEncoder.encode(fullAddr, "UTF-8")
            val addr = "$apiUrl?query=$encodedAddr"
            val url = URL(addr)
            val conn: URLConnection = url.openConnection()
            conn.setRequestProperty("Authorization", "KakaoAK $apiKey")

            val json = BufferedReader(InputStreamReader(conn.getInputStream(), "UTF-8"))
            val docJson = StringBuffer()
            var line: String?
            while (json.readLine().also { line = it } != null) {
                docJson.append(line)
            }

            jsonString = docJson.toString()
            json.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return jsonString
    }

    // 카카오 API 응답을 받아 위도와 경도를 추출하는 함수
//    fun changeToJSON(jsonString: String?): ArrayList<Double> {
//        val array = ArrayList<Double>()
//
//        val parser = JSONParser()
//
//        try {
//            val document = parser.parse(jsonString) as JSONObject
//            val jsonArray = document["documents"] as JSONArray
//            val position = jsonArray[0] as JSONObject
//
//
//
//            val lon = (position["x"] as String).toDouble()
//            val lat = (position["y"] as String).toDouble()
//
//            array.add(lon)
//            array.add(lat)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        return array
//    }

    fun changeToJSON(jsonString: String?): ArrayList<Double> {
        val array = ArrayList<Double>()

        if (jsonString.isNullOrEmpty()) return array  // Null 또는 빈 문자열 방지

        val parser = JSONParser()

        try {
            val document = parser.parse(jsonString) as JSONObject
            val jsonArray = document["documents"] as? JSONArray ?: return array  // 안전한 캐스팅

            if (jsonArray.isNotEmpty()) {
                val position = jsonArray[0] as? JSONObject ?: return array

                val lon = position["x"]?.toString()?.toDoubleOrNull()
                val lat = position["y"]?.toString()?.toDoubleOrNull()

                if (lon != null && lat != null) {
                    array.add(lon)
                    array.add(lat)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return array
    }
}

