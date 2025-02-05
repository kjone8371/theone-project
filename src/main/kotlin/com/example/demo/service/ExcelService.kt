
//import com.example.demo.dto.FacilityCreateDto
//import com.example.demo.dto.FacilityExcelDto
//import com.example.demo.dto.FacilityTypeConverter
//import com.example.demo.entity.Facility
//import com.example.demo.entity.FacilityStatus
//import com.example.demo.entity.FacilityType
//import com.example.demo.repository.FacilityRepository
//import org.apache.poi.ss.usermodel.Cell
//import org.apache.poi.ss.usermodel.CellType
//import org.apache.poi.ss.usermodel.WorkbookFactory
//import org.springframework.stereotype.Service
//import org.springframework.web.multipart.MultipartFile
//import java.io.InputStream
//
//@Service
//class ExcelService(private val facilityRepository: FacilityRepository, private val kakaoService: KakaoService) {
//
//    fun getStringValue(cell: Cell?): String {
//        return when (cell?.cellType) {
//            CellType.STRING -> cell.stringCellValue.trim()  // 문자열 그대로 가져옴
//            CellType.NUMERIC -> cell.numericCellValue.toString().trim()  // 숫자는 문자열로 변환
//            CellType.BOOLEAN -> cell.booleanCellValue.toString().trim()  // 불린 값도 변환
//            else -> ""  // 비어 있으면 빈 문자열 반환
//        }
//    }
//
//    fun saveFacilitiesFromExcel(multipartFile: MultipartFile) {
//        val inputStream: InputStream = multipartFile.inputStream
//        val workbook = WorkbookFactory.create(inputStream)
//        val sheet = workbook.getSheetAt(0)
//        val facilities = mutableListOf<Facility>()
//
//        try {
//            val rowIterator = sheet.iterator()
//            if (rowIterator.hasNext()) rowIterator.next() // 헤더 스킵
//
//            while (rowIterator.hasNext()) {
//                val row = rowIterator.next()
//                try {
//                    val facilityExcelDto = FacilityExcelDto(
//                        name = getStringValue(row.getCell(0)),               // 시설물 명치
//                        address = getStringValue(row.getCell(1)),            // 도로명 주소
//                        phoneNumber = getStringValue(row.getCell(2)),        // 전화번호
//                        type = FacilityType.fromDisplayName(getStringValue(row.getCell(3))), // 조명 구분
//                        poleFormat = getStringValue(row.getCell(4)),        // 전주번호
//                        fixture = getStringValue(row.getCell(5)),            // 등기구 형태
//                        dimmer = getStringValue(row.getCell(6)),             // 점멸기
//                        escoStatus = getStringValue(row.getCell(7)),         // ESCO
//                        powerConsumption = getStringValue(row.getCell(8)),   // 소비 전력
//                        billingType = getStringValue(row.getCell(9)),        // 요금 형태
//                        department = getStringValue(row.getCell(10)),        // 관리부서
//                        poleNumber = getStringValue(row.getCell(11)),        // 전주 번호
//                        memo = getStringValue(row.getCell(12))
//
//                    )
//
//                    // 주소로부터 경도와 위도를 가져오기 위한 카카오 API 호출
//                    val json = kakaoService.getKakaoApiFromAddress(facilityExcelDto.address)
//                    val coordinates = kakaoService.changeToJSON(json)
//
//                    val facility = Facility(
//                        name = facilityExcelDto.name,
//                        type = facilityExcelDto.type,
//                        status = null,
//                        address = facilityExcelDto.address,
//                        latitude = coordinates[1],   // 위도
//                        longitude = coordinates[0],  // 경도
//                        department = facilityExcelDto.department,
//                        fixture = facilityExcelDto.fixture,
//                        poleFormat = facilityExcelDto.poleFormat,
//                        dimmer = facilityExcelDto.dimmer,
//                        memo = facilityExcelDto.memo,
//                        phoneNumber = facilityExcelDto.phoneNumber,
//                        escoStatus = facilityExcelDto.escoStatus,
//                        powerConsumption = facilityExcelDto.powerConsumption,
//                        billingType = facilityExcelDto.billingType,
//                        poleNumber = facilityExcelDto.poleNumber,
//                        image = null
//                    )
//                    facilities.add(facility)
//
//                } catch (e: Exception) {
//                    null
//                }?: continue
//            }
//
//            facilityRepository.saveAll(facilities)
//            println("✅ ${facilities.size}개의 시설 데이터 저장 완료!")
//        } finally {
//            workbook.close()
//        }
//    }
//
//}


package com.example.demo.service

import com.example.demo.dto.FacilityExcelDto
import com.example.demo.dto.FacilityTypeConverter
import com.example.demo.entity.Facility
import com.example.demo.repository.FacilityRepository
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import java.io.FileOutputStream

@Service
class ExcelService(
    private val facilityRepository: FacilityRepository,
    private val kakaoService: KakaoService // KakaoService 주입
) {

    // 셀의 값을 문자열로 가져오는 함수
    fun getStringValue(cell: Cell?): String {
        return when (cell?.cellType) {
            CellType.STRING -> cell.stringCellValue.trim()  // 문자열 그대로 가져옴
            CellType.NUMERIC -> cell.numericCellValue.toString().trim()  // 숫자는 문자열로 변환
            CellType.BOOLEAN -> cell.booleanCellValue.toString().trim()  // 불린 값도 변환
            else -> ""  // 비어 있으면 빈 문자열 반환
        }
    }

    // 엑셀 파일을 읽어 시설 정보를 DB에 저장하는 함수
    fun saveFacilitiesFromExcel(multipartFile: MultipartFile) {
        val inputStream: InputStream = multipartFile.inputStream
        val workbook = WorkbookFactory.create(inputStream)
        val sheet = workbook.getSheetAt(0)
        val facilities = mutableListOf<Facility>()

        try {
            val rowIterator = sheet.iterator()
            if (rowIterator.hasNext()) rowIterator.next() // 헤더 스킵

            while (rowIterator.hasNext()) {
                val row = rowIterator.next()
                try {
                        // 엑셀에서 데이터를 읽어 FacilityExcelDto 객체 생성
                        val facilityExcelDto = FacilityExcelDto(
                            name = getStringValue(row.getCell(0)),               // 시설물 명치
                            address = getStringValue(row.getCell(1)),            // 도로명 주소
                            phoneNumber = getStringValue(row.getCell(2)),        // 전화번호
                            //type = FacilityType.fromDisplayName(getStringValue(row.getCell(3)).trim()), // 조명 구분
                            type = FacilityTypeConverter.getFacilityTypeFromDisplayName(getStringValue(row.getCell(3))), // 시설 유형 (예: 조명 구분)
                            poleFormat = getStringValue(row.getCell(4)),        // 전주번호
                            fixture = getStringValue(row.getCell(5)),            // 등기구 형태
                            dimmer = getStringValue(row.getCell(6)),             // 점멸기
                            escoStatus = getStringValue(row.getCell(7)),         // ESCO
                            powerConsumption = getStringValue(row.getCell(8)),   // 소비 전력
                            billingType = getStringValue(row.getCell(9)),        // 요금 형태
                            department = getStringValue(row.getCell(10)),        // 관리부서
                            poleNumber = getStringValue(row.getCell(11)),        // 전주 번호
                            memo = getStringValue(row.getCell(12))               // 메모
                        )

                    // 주소로부터 경도와 위도를 가져오기 위한 카카오 API 호출
                    val json = kakaoService.getKakaoApiFromAddress(facilityExcelDto.address)
                    val coordinates = kakaoService.changeToJSON(json)

                    // 경도와 위도가 정상적으로 추출되었으면 시설 정보 저장
                    if (coordinates.isNotEmpty()) {
                        val facility = Facility(
                            name = facilityExcelDto.name,
                            type = facilityExcelDto.type,
                            status = null,
                            address = facilityExcelDto.address,
                            latitude = coordinates[1],   // 위도
                            longitude = coordinates[0],  // 경도
                            department = facilityExcelDto.department,
                            fixture = facilityExcelDto.fixture,
                            poleFormat = facilityExcelDto.poleFormat,
                            dimmer = facilityExcelDto.dimmer,
                            memo = facilityExcelDto.memo,
                            phoneNumber = facilityExcelDto.phoneNumber,
                            escoStatus = facilityExcelDto.escoStatus,
                            powerConsumption = facilityExcelDto.powerConsumption,
                            billingType = facilityExcelDto.billingType,
                            poleNumber = facilityExcelDto.poleNumber,
                            image = null
                        )
                        facilities.add(facility)
                    }

                } catch (e: Exception) {
                    // 예외 발생 시, 해당 행을 건너뜀
                    continue
                }
            }

            // DB에 저장
            facilityRepository.saveAll(facilities)
            println("✅ ${facilities.size}개의 시설 데이터 저장 완료!")

        } finally {
            workbook.close() // 자원 해제
        }
    }


//    fun exportDataToExcel(filePath: String) {
//        val workbook = XSSFWorkbook()
//        val sheet = workbook.createSheet("Data")
//
//        // 헤더 설정
//        val headers = listOf(
//            "ID", "이름", "주소", "전화번호", "설치물", "종류", "램프 종류", "통신 방식",
//            "비고", "전력", "요금제", "회사명", "기기 ID", "추가 정보"
//        )
//
//        val headerRow = sheet.createRow(0)
//        headers.forEachIndexed { index, header ->
//            headerRow.createCell(index).setCellValue(header)
//        }
//
//        // 데이터베이스에서 데이터 조회
//        val dataList = facilityRepository.findAll()
//
//        // 데이터 추가
//        dataList.forEachIndexed { rowIndex, data ->
//            val row = sheet.createRow(rowIndex + 1)
//            row.createCell(0).setCellValue(data.name)
//            row.createCell(1).setCellValue(data.address)
//            row.createCell(2).setCellValue(data.phoneNumber)
//            row.createCell(3).setCellValue(data.type.toString())
//            row.createCell(4).setCellValue(data.poleFormat)
//            row.createCell(5).setCellValue(data.fixture)
//            row.createCell(6).setCellValue(data.dimmer)
//            row.createCell(7).setCellValue(data.escoStatus)
//            row.createCell(8).setCellValue(data.powerConsumption)
//            row.createCell(9).setCellValue(data.billingType)
//            row.createCell(10).setCellValue(data.department)
//            row.createCell(11).setCellValue(data.poleNumber)
//            row.createCell(12).setCellValue(data.memo)
//        }
//
//        // 파일 저장
//        FileOutputStream(filePath).use { outputStream ->
//            workbook.write(outputStream)
//        }
//        workbook.close()
//
//        println("Excel 파일이 저장되었습니다: $filePath")
//    }

    fun exportDataToExcel(filePath: String, facilityId: Long) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Data")

        // 헤더 설정
        val headers = listOf(
            "ID", "이름", "주소", "전화번호", "설치물", "종류", "램프 종류", "통신 방식",
            "비고", "전력", "요금제", "회사명", "기기 ID", "추가 정보"
        )

        val headerRow = sheet.createRow(0)
        headers.forEachIndexed { index, header ->
            headerRow.createCell(index).setCellValue(header)
        }

        // 데이터 가져오기
        val optionalFacility = facilityRepository.findById(facilityId)

        // 데이터가 존재할 경우 데이터 추가
        optionalFacility.ifPresent { facility ->
            val row = sheet.createRow(1) // rowIndex + 1
            row.createCell(0).setCellValue(facility.name)
            row.createCell(1).setCellValue(facility.address)
            row.createCell(2).setCellValue(facility.phoneNumber)
            row.createCell(3).setCellValue(facility.type.toString())
            row.createCell(4).setCellValue(facility.poleFormat)
            row.createCell(5).setCellValue(facility.fixture)
            row.createCell(6).setCellValue(facility.dimmer)
            row.createCell(7).setCellValue(facility.escoStatus)
            row.createCell(8).setCellValue(facility.powerConsumption)
            row.createCell(9).setCellValue(facility.billingType)
            row.createCell(10).setCellValue(facility.department)
            row.createCell(11).setCellValue(facility.poleNumber)
            row.createCell(12).setCellValue(facility.memo)

            // 파일 저장
            FileOutputStream(filePath).use { outputStream ->
                workbook.write(outputStream)
            }
            workbook.close()

            println("Excel 파일이 저장되었습니다: $filePath")
        } ?: run {
            // 데이터가 없는 경우 처리
            println("해당 Facility ID에 대한 데이터를 찾을 수 없습니다.")
        }
    }
}
