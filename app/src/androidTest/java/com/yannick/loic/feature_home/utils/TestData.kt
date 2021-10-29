package com.yannick.loic.feature_home.utils

import com.yannick.loic.remotedata.models.Comic
import com.yannick.loic.remotedata.models.DataContainer
import com.yannick.loic.remotedata.models.DataWrapper
import com.yannick.loic.remotedata.models.Image

val image1 = Image(
    path = "file:///android_asset/comic1",
    extension = "jpg"
)

val image2 = Image(
    path = "file:///android_asset/comic2",
    extension = "jpg"
)

val comic1 = Comic(
    id = 100,
    title = "Avengers Fake title 1",
    description = "Fake description 1",
    thumbnail = image1,
    images = listOf(image1)
)

val comic2 = comic1.copy(
    id = 101,
    title = "Avengers Fake title 2",
    description = "Fake description 2",
    thumbnail = image2,
    images = listOf(image2)
)

val container = DataContainer(
    offset = 0,
    limit = 10,
    total = 2,
    count = 2,
    results = listOf(
        comic1, comic2
    )
)

val dataWrapper = DataWrapper(
    code = 0,
    status = "200",
    copyRight = "@Marvel",
    attributionText = "dummy",
    attributionHTML = "dummy",
    etag = "dummy",
    data = container
)

