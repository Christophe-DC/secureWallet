package com.cdcoding.model

import kotlinx.serialization.Serializable

@Serializable
data class StakeValidator (
	val id: String,
	val name: String
)

