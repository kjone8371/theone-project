package com.example.demo.entity

import org.springframework.data.jpa.repository.JpaRepository
import kotlin.jvm.optionals.getOrNull

sealed interface IdWrapper<ID : Any> {
    val get: ID
    fun <T : Any> fetch(repository: JpaRepository<T, ID>) = repository.findById(get).getOrNull()
}