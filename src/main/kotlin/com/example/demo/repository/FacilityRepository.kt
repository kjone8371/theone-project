package com.example.demo.repository

import com.example.demo.entity.Facility
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface FacilityRepository : JpaRepository<Facility, Long>{
    fun findFacilitiesByIdIn(ids: List<Long>): List<Facility>

    fun findByName(name: String): List<Facility>

    override fun findById(id: Long): Optional<Facility>
}
