package com.github.olegzuev.yukarinotes.ui.secretdungeon

import androidx.lifecycle.ViewModel
import com.github.olegzuev.yukarinotes.data.WaveGroup
import com.github.olegzuev.yukarinotes.ui.base.*
import com.github.olegzuev.yukarinotes.ui.shared.SharedViewModelSecretDungeon

class SecretDungeonWaveViewModel(
    private val sharedSecretDungeon: SharedViewModelSecretDungeon
) : ViewModel() {
        val viewList = mutableListOf<ViewType<*>>()
            get() {
                field.clear()
                sharedSecretDungeon.selectedSchedule?.let { schedule ->
                    schedule.waveGroupMap.forEach {
                        field.add(SecretDungeonQuestVT(it))
                    }
                }
                return field
            }
}

interface OnSecretDungeonQuestClickListener: OnItemActionListener {
    fun onSecretDungeonQuestClicked(waveGroup: WaveGroup)
}
