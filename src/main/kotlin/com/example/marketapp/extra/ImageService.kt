package com.example.marketapp.extra

import com.example.marketapp.exception.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.util.*


@Service
class ImageService(
    private val imageRepository: ImageRepository,
) {
    @Value(value = "\${source.api.imageUrl}")
    val imageUrl: String? = null

    suspend fun save(file: FilePart): String? {
        return withContext(Dispatchers.IO) {
            val name = StringUtils.cleanPath(file.filename())
            try {
                if (name.contains("..")) {
                    throw ApiException("Filename invalid")
                }

                val type = if (name.endsWith(".jpg")) {
                    MediaType.IMAGE_JPEG_VALUE
                } else if (name.endsWith(".png")) {
                    MediaType.IMAGE_PNG_VALUE
                } else {
                    throw ApiException("Invalid FileType")
                }
                val img = file.content().blockFirst()!!.asInputStream().readAllBytes()
                val image = Image(
                    image = img,
                    type = type,
                    name = UUID.randomUUID().toString() + name
                )
                return@withContext imageUrl + "${imageRepository.save(image).id}"
            } catch (e: Exception) {
                throw ApiException("An error occurred while recording an image! {${e.message!!}}")
            }
        }

    }

    suspend fun findByid(id: Int) = withContext(Dispatchers.IO) {
        imageRepository.findById(id)
    }

    suspend fun deleteById(id: Int) = withContext(Dispatchers.IO) {
        imageRepository.removeById(id = id)
    }

}