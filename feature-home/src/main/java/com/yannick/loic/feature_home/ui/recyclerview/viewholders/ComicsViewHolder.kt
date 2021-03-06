package com.yannick.loic.feature_home.ui.recyclerview.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.yannick.loic.feature_home.databinding.ItemViewBinding
import com.yannick.loic.feature_home.ui.bindings.ComicsDataBindingModel
import com.yannick.loic.feature_home.ui.mapper.ComicsUIEntityMapper
import com.yannick.loic.home.domain.entities.ComicsEntity

class ComicsViewHolder(val binding: ItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        comicsEntity: ComicsEntity,
        comicsUIEntityMapper: ComicsUIEntityMapper
    ) {
        if (binding.comicsUiModelObserver == null)
            binding.comicsUiModelObserver =
                ComicsDataBindingModel(comicsUIEntityMapper.to(comicsEntity), comicsUIEntityMapper)
        else
            binding.comicsUiModelObserver!!.comics = comicsUIEntityMapper.to(comicsEntity)
    }
}
