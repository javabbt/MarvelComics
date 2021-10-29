package com.yannick.loic.localdata.mapper

import com.yannick.loic.home.domain.entities.ComicsEntity
import com.yannick.loic.localdata.models.ComicsLocal
import com.yannick.loic.utils.Mapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComicsLocalMapper @Inject constructor() : Mapper<ComicsEntity, ComicsLocal> {
    override fun from(model: ComicsLocal): ComicsEntity {
        return ComicsEntity(
            id = model.id,
            title = model.title,
            description = model.description,
            thumbNail = model.thumbNail,
            imageUrls = model.imageUrls,
            flagged = model.flagged
        )
    }

    override fun to(entity: ComicsEntity): ComicsLocal {
        return ComicsLocal(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            thumbNail = entity.thumbNail,
            imageUrls = entity.imageUrls,
            flagged = entity.flagged
        )
    }

}
