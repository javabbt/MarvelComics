package com.yannick.loic.feature_home.ui.mapper

import com.yannick.loic.feature_home.ui.models.ComicsUIModel
import com.yannick.loic.home.domain.entities.ComicsEntity
import com.yannick.loic.utils.Mapper


class ComicsUIEntityMapper : Mapper<ComicsEntity, ComicsUIModel> {
    //conversion du modele a l'entite
    override fun from(model: ComicsUIModel): ComicsEntity {
        return ComicsEntity(
            id = model.id,
            title = model.title,
            description = model.description,
            thumbNail = model.thumbNail,
            imageUrls = model.imageUrls,
            flagged = model.flagged
        )
    }

    //conversion de l'entite au modele
    override fun to(entity: ComicsEntity): ComicsUIModel {
        return ComicsUIModel(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            thumbNail = entity.thumbNail,
            imageUrls = entity.imageUrls,
            flagged = entity.flagged
        )
    }

}