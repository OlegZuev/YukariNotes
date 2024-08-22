package com.github.olegzuev.yukarinotes.ui.drop

import androidx.lifecycle.ViewModel
import com.github.olegzuev.yukarinotes.data.Equipment

class DropViewModel : ViewModel() {

    var itemList = mutableListOf<Any>()

    fun refreshList(equipList: List<Equipment>) {
        itemList.clear()
        var currentRarity = 0
        equipList.forEach {
            if (it.itemId != 999999) {
                if (currentRarity != it.rarity) {
                    currentRarity = it.rarity
                    itemList.add(currentRarity.toString())
                }
                itemList.add(it)
            }
        }
    }
}