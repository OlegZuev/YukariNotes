package com.github.olegzuev.yukarinotes.data

import com.github.olegzuev.yukarinotes.common.Statics

class GeneralItem(
    override val itemId: Int,
    override val itemName: String,
    override val itemType: ItemType
) : Item {
    override val iconUrl: String = Statics.ITEM_ICON_URL.format(itemId)
}