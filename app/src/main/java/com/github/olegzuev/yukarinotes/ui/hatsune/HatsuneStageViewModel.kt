package com.github.olegzuev.yukarinotes.ui.hatsune

import androidx.lifecycle.ViewModel
import com.github.olegzuev.yukarinotes.ui.base.HatsuneStageVT
import com.github.olegzuev.yukarinotes.ui.base.OnItemActionListener
import com.github.olegzuev.yukarinotes.ui.base.ViewType
import com.github.olegzuev.yukarinotes.ui.shared.SharedViewModelHatsune

class HatsuneStageViewModel(
    private val sharedHatsune: SharedViewModelHatsune
) : ViewModel() {

    val viewList = mutableListOf<ViewType<*>>()
        get() {
            field.clear()
            sharedHatsune.hatsuneStageList.value?.forEach {
                field.add(HatsuneStageVT(it))
            }
            return field
        }
}

interface OnHatsuneClickListener<T>: OnItemActionListener {
    fun onStageClicked(item: T)
}