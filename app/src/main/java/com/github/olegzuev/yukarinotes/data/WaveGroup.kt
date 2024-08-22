package com.github.olegzuev.yukarinotes.data

import com.github.olegzuev.yukarinotes.R


class WaveGroup(
    val id: Int,
    val waveGroupId: Int
) {
    var enemyList = listOf<Enemy>()
    var dropGoldList: List<Int>? = null
    var dropRewardList: List<EnemyRewardData>? = null

    fun getEnemyIconUrl(position: Int): String {
        return if (position >= 0 && position < enemyList.size) {
            enemyList[position].iconUrl
        } else "drawable://" + R.drawable.mic_chara_icon_place_holder
    }
}