package com.example.demo.controller

import com.example.demo.dto.FacilityDto
import com.example.demo.entity.Facility
import com.example.demo.entity.FacilityType
import com.example.demo.service.ExcelService
import com.example.demo.service.KakaoService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.InputStream
import java.nio.file.Paths

@RestController
@RequestMapping("/api/facilities")
class ExcelController(private val excelService: ExcelService, private val kakaoService: KakaoService,) {

    @Operation(
        summary = "엑셀 파일 업로드",
        description = "엑셀 파일을 업로드하여 시설 정보를 저장합니다."
    )
    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadExcel(
        @RequestParam("file") file: MultipartFile // file 파라미터로 받을 경우
    ): ResponseEntity<String> {
        return try {
            excelService.saveFacilitiesFromExcel(file) // 서비스 메서드 호출
            ResponseEntity.ok("Excel 데이터가 성공적으로 저장되었습니다.")
        } catch (e: Exception) {
            ResponseEntity.badRequest().body("오류 발생: ${e.message}")
        }
    }

    @GetMapping("/download")
    fun downloadExcel(@RequestParam(required = false) facilityId: Long): ResponseEntity<ByteArray> {
        val filePath = Paths.get(System.getProperty("user.dir"), "database_data.xlsx").toString()

        // 엑셀 파일 생성 및 저장
        excelService.exportDataToExcel(filePath, facilityId)

        // 파일을 ByteArray로 읽어오기
        val fileBytes = File(filePath).readBytes()

        // 파일을 클라이언트에 다운로드할 수 있도록 반환
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=database_data.xlsx")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(fileBytes)
    }

//    // 엑셀 파일을 업로드하고, 주소를 경도, 위도로 변환 후 DB에 저장하는 엔드포인트
//    @PostMapping("/uploadExcel")
//    fun uploadExcel(@RequestParam("file") file: MultipartFile): String {
//        try {
//            // 엑셀 파일 읽기
//            val inputStream: InputStream = file.inputStream
//            val workbook = WorkbookFactory.create(inputStream) as XSSFWorkbook
//            val sheet = workbook.getSheetAt(0)
//
//            // 엑셀 첫 번째 행은 헤더이므로 1부터 시작
//            for (row in sheet) {
//                if (row.rowNum == 0) continue  // 첫 번째 행은 헤더이므로 건너뜁니다.
//
//                val address = row.getCell(0)?.stringCellValue ?: continue  // 첫 번째 열이 주소라고 가정
//
//                // 주소를 경도, 위도로 변환
//                val json = kakaoService.getKakaoApiFromAddress(address)
//                val coordinates = kakaoService.changeToJSON(json)
//
//                // 경도와 위도가 정상적으로 추출되지 않으면 해당 주소는 건너뜀
//                if (coordinates.isNotEmpty()) {
//                    // DB에 저장 (경도와 위도 추가)
//                    val facility = FacilityDto(
//                        address = address,
//                        longitude = coordinates[0],
//                        latitude = coordinates[1]
//                    )
//                    excelService.saveFacilitiesFromExcel(facility)  // DB에 저장
//                }
//            }
//
//            return "파일 처리 완료"
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return "파일 처리 중 오류가 발생했습니다."
//        }
//    }
}
