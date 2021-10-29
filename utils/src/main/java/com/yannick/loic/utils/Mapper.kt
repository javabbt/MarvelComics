package com.yannick.loic.utils


interface Mapper<Entity, Model> {

    fun from(model: Model): Entity

    fun to(entity: Entity): Model

}
