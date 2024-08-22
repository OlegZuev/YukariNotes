package com.github.olegzuev.yukarinotes.data

import com.github.olegzuev.yukarinotes.R
import com.github.olegzuev.yukarinotes.common.I18N
import com.github.olegzuev.yukarinotes.common.Statics

class RewardData(
    val rewardType: Int,
    val rewardId: Int,
    val rewardNum: Int,
    val odds: Int
) {
    val rewardIcon: String = Statics.EQUIPMENT_ICON_URL.format(rewardId)
    val oddsString: String = I18N.getString(R.string.percent_modifier).format(odds)

}