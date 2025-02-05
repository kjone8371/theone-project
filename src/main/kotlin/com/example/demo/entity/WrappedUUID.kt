package com.example.demo.entity

import java.util.*

@JvmInline
value class WrappedUUID(private val _id: UUID?) : IdWrapper<UUID> {
    override val get: UUID get() = _id!!

    companion object {
        val NULL = WrappedUUID(null)
    }
}