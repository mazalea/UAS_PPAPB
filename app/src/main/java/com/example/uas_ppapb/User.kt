package com.example.uas_ppapb

data class User(
    val email : String ?= null,
    val password : String ?= null,
    val role : String = "public"
)
