package com.example.demo.entity

@JvmInline
value class WrappedLong(private val _id: Long?) : IdWrapper<Long> {
    override val get: Long get() = _id!!

    companion object {
        val NULL = WrappedLong(null)
    }
}