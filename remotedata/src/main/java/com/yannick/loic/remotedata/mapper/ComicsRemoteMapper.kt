package com.yannick.loic.remotedata.mapper

import com.yannick.loic.home.domain.entities.ComicsEntity
import com.yannick.loic.remotedata.models.Comic
import com.yannick.loic.remotedata.models.Image
import com.yannick.loic.utils.Mapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComicsRemoteMapper @Inject constructor() : Mapper<ComicsEntity, Comic> {
    override fun from(model: Comic): ComicsEntity {
        return ComicsEntity(
            id = model.id.toString(),
            title = model.title,
            description = model.description ?: "Desole!! La description arrive bientot!!",
            thumbNail = with(model.thumbnail) { "$path.$extension" },
            imageUrls = model.images.map { "${it.path}.${it.extension}" },
            flagged = false
        )
    }

    override fun to(entity: ComicsEntity): Comic {
        return Comic(
            id = entity.id.toInt(),
            title = entity.title,
            description = entity.description,
            thumbnail = getImage(entity.thumbNail),
            images = entity.imageUrls.map { getImage(it) }
        )
    }

    private fun getImage(str: String): Image {
        val (path, ext) = str.split(".")
        return Image(path = path, extension = ext)
    }
}