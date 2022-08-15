package com.example.marketapp.service

import com.example.marketapp.model.Address
import com.example.marketapp.repository.LocationRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class LocationService(
    private val repository: LocationRepository,
) {

    suspend fun save(address: Address): Address {
        return repository.save(entity = address)
    }

    suspend fun findUserLocation(userId: String): Address? {
        val id = UUID.fromString(userId)
        return repository.findByUserId(userId = id)
    }

    suspend fun remove(id: Int): Boolean {
        return repository.delete(id) == 1
    }

}