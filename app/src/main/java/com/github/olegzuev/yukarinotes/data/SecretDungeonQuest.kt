package com.github.olegzuev.yukarinotes.data

class SecretDungeonQuest(
    val questId: Int,
    val dungeonAreaId: Int,
    val difficulty: Int,
    val floorNum: Int,
    val questType: Int,
    val waveGroupId: Int,
    val waveGroup: WaveGroup
) {
}