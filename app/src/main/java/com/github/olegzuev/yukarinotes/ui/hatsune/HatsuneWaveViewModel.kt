package com.github.olegzuev.yukarinotes.ui.hatsune

import androidx.lifecycle.ViewModel
import com.github.olegzuev.yukarinotes.data.WaveGroup
import com.github.olegzuev.yukarinotes.ui.base.HatsuneWaveVT
import com.github.olegzuev.yukarinotes.ui.base.OnItemActionListener
import com.github.olegzuev.yukarinotes.ui.base.ViewType
import com.github.olegzuev.yukarinotes.ui.shared.SharedViewModelHatsune

class HatsuneWaveViewModel(
    private val sharedHatsune: SharedViewModelHatsune
) : ViewModel() {

    val viewList = mutableListOf<ViewType<*>>()
        get() {
            field.clear()
            sharedHatsune.selectedHatsune?.let { stage ->
                stage.battleWaveGroupMap.forEach {
                    field.add(HatsuneWaveVT(it))
                }
            }
            return field
        }
}

interface OnWaveClickListener : OnItemActionListener {
    fun onWaveClick(waveGroup: WaveGroup)
}