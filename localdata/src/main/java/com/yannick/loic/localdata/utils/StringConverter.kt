package com.yannick.loic.localdata.utils

import androidx.room.TypeConverter

open class StringConverter {
    @TypeConverter
    fun fromList(strings: List<String>) = strings.joinToString(",")

    @TypeConverter
    fun toList(string: String) = string.split(",").toList()
}
