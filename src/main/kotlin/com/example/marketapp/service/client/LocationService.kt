package com.example.marketapp.service.client

import com.example.marketapp.model.client.Address
import com.example.marketapp.repository.client.LocationRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class LocationService(
    private val repository: LocationRepository,
) {

    suspend fun save(address: Address): Address {
        val address1 = findUserLocation(address.userId.toString())
        if (address1 != null) {
            address.id = address1.id
        }
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