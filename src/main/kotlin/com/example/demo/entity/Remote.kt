package com.example.demo.entity

import jakarta.persistence.*

@Entity
@Table(name = "tb_remote")
class Remote(
    val provider: RemoteProvider,

    val address: String, // almost IoT Device uses mqtt protocol.
    val port: Int,
    val username: String?,
    val password: String?,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: WrappedLong = WrappedLong.NULL,
)