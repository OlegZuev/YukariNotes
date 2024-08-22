package com.github.olegzuev.yukarinotes.db

import com.github.olegzuev.yukarinotes.R
import com.github.olegzuev.yukarinotes.common.I18N
import com.github.olegzuev.yukarinotes.data.*

class MasterUniqueEquipment {
    fun getCharaUniqueEquipment(chara: Chara): Equipment? {
        return DBInfo.rawUniqueEquipmentDataMap[chara.unitId]?.let {
            val itemIdList = it.itemIdList
            val consumeIdList = it.consumeIdList
            it.getCharaUniqueEquipment(chara).apply {
                val map = mutableMapOf<Item, Int>()
                for (i in 0..9) {
                    val itemId = itemIdList[i]
                    if (itemId == 140000) {
                        map[EquipmentPiece(itemId, I18N.getString(R.string.princess_heart))] = consumeIdList[i]
                    } else if (itemId in 25000..39999) {
                        map[GeneralItem(itemId, I18N.getString(R.string.memory_piece), ItemType.GENERAL_ITEM)] = consumeIdList[i]
                    }
                }
                craftMap = map
            }
        }
    }
}