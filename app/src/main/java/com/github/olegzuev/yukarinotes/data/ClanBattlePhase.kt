package com.github.olegzuev.yukarinotes.data

import com.github.olegzuev.yukarinotes.db.DBInfo

class ClanBattlePhase(
    val phase: Int,
    val waveGroupId1: Int,
    val waveGroupId2: Int,
    val waveGroupId3: Int,
    val waveGroupId4: Int,
    val waveGroupId5: Int) {

    val bossList = mutableListOf<Enemy>()

    init {
        val waveGroupList = mutableListOf<WaveGroup>()
        listOf(
            waveGroupId1, waveGroupId2, waveGroupId3, waveGroupId4, waveGroupId5
        ).filter { it != 0 }.forEach { waveGroupId ->
            DBInfo.getRawWaveGroup(waveGroupId)?.let {
                waveGroupList.add(it.getWaveGroup(true))
            }
        }

        waveGroupList.forEach{ w ->
            w.enemyList.forEach { bossList.add(it) }
        }
    }

}