package com.yannick.loic.remotedata.datasource.utils

import com.yannick.loic.home.domain.entities.ComicsEntity
import com.yannick.loic.remotedata.mapper.ComicsRemoteMapper
import com.yannick.loic.remotedata.models.Comic
import com.yannick.loic.remotedata.models.DataContainer
import com.yannick.loic.remotedata.models.DataWrapper

class TestRemoteDataContainer {
    companion object {
        private val comicsRemoteMapper = ComicsRemoteMapper()
        fun getComicsList(): List<Comic> {
            return getComicsEntityList().map { comicsRemoteMapper.to(it) }
        }

        fun getComic(): Comic {
            return comicsRemoteMapper.to(getComicsEntity())
        }

        private fun getComicsEntity(): ComicsEntity {
            return ComicsEntity(
                "143",
                "Startling Stories: The Incorrigible Hulk (2004) #1",
                "For Doctor Bruce Banner life is anything but normal. But what happens when two women get between him and his alter ego, the Incorrigible Hulk? Hulk confused! \\r\\nIndy superstar Peter Bagge (THE MEGALOMANIACAL SPIDER-MAN) takes a satirical jab at the Hulk mythos with a tale of dames, debauchery and destruction.",
                "thumbnail.jpg",
                listOf("image.jg"),
                true
            )
        }

        private fun getComicsEntityList() =
            listOf(
                getComicsEntity(),
                getComicsEntity().copy(id = "123"),
                getComicsEntity().copy(id = "134")
            )

        fun getDataWrapper(comicsList: List<Comic>? = null): DataWrapper<List<Comic>> {
            return DataWrapper(
                code = 0,
                status = "",
                copyRight = "",
                attributionHTML = "",
                attributionText = "",
                etag = "",
                data = DataContainer(0, 40, 3, 3, comicsList?.let { it } ?: getComicsList())
            )
        }
    }
}