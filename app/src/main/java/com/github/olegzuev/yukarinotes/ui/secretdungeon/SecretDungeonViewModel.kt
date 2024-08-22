package com.github.olegzuev.yukarinotes.ui.secretdungeon

import androidx.lifecycle.ViewModel
import com.github.olegzuev.yukarinotes.ui.base.OnItemActionListener
import com.github.olegzuev.yukarinotes.ui.base.SecretDungeonScheduleVT
import com.github.olegzuev.yukarinotes.ui.base.ViewType
import com.github.olegzuev.yukarinotes.ui.shared.SharedViewModelSecretDungeon

class SecretDungeonViewModel(
    private val sharedSecretDungeon: SharedViewModelSecretDungeon
) : ViewModel() {
        val viewList = mutableListOf<ViewType<*>>()
        get() {
            field.clear()
            sharedSecretDungeon.secretDungeonScheduleList.value?.forEach {
                field.add(SecretDungeonScheduleVT(it))
            }
            return field
        }
}

interface OnSecretDungeonScheduleClickListener<T>: OnItemActionListener {
    fun onSecretDungeonScheduleClicked(item: T)
}