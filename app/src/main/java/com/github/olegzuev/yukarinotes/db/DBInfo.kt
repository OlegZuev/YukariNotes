package com.github.olegzuev.yukarinotes.db

object DBInfo {
    // Characters
    val rawUnitRarityMap: MutableMap<Int, List<RawUnitRarity>> = mutableMapOf()
    val rawCharaStoryStatusMap: MutableMap<Int, List<RawCharaStoryStatus>> = mutableMapOf()
    val rawPromotionStatusMap: MutableMap<Int, List<RawPromotionStatus>> = mutableMapOf()
    val rawPromotionBonusMap: MutableMap<Int, List<RawPromotionBonus>> = mutableMapOf()
    val rawUnitPromotionMap: MutableMap<Int, List<RawUnitPromotion>> = mutableMapOf()
    val rawUniqueEquipment1DataMap: MutableMap<Int, RawUniqueEquipmentData> = mutableMapOf()
    val rawUniqueEquipment2DataMap: MutableMap<Int, RawUniqueEquipmentData> = mutableMapOf()
    val rawUniqueEquipmentEnhanceData: MutableMap<Int, List<RawUniqueEquipmentEnhanceData>> = mutableMapOf()
    val rawUnitSkillDataMap: MutableMap<Int, RawUnitSkillData> = mutableMapOf()
    private val rawSkillDataMap: MutableMap<Int, RawSkillData> = mutableMapOf()
    private val rawSkillActionMap: MutableMap<Int, RawSkillAction> = mutableMapOf()
    val rawUnitMinionMap: MutableMap<Int, RawUnitMinion> = mutableMapOf()
    private val rawUnitAttackPatternMap: MutableMap<Int, List<RawUnitAttackPattern>> = mutableMapOf()

    // Clan Battle
    val rawClanBattlePhaseMap: MutableMap<Int, List<RawClanBattlePhase>> = mutableMapOf()
    val rawWaveGroupClanBattleMap: MutableMap<Int, RawWaveGroup> = mutableMapOf()
    private val rawEnemyClanBattleMap: MutableMap<Int, RawEnemy> = mutableMapOf()
    val rawResistDataMap: MutableMap<Int, RawResistData> = mutableMapOf()
    val rawAilmentMap: MutableMap<Int, String> = mutableMapOf()

    fun initCharactersInfo() {
        rawUnitRarityMap += DBHelper.get().getUnitRarityMap()
        rawCharaStoryStatusMap += DBHelper.get().getCharaStoryStatusMap()
        rawPromotionStatusMap += DBHelper.get().getCharaPromotionStatusMap()
        rawPromotionBonusMap += DBHelper.get().getCharaPromotionBonusMap()
        rawUnitPromotionMap += DBHelper.get().getCharaPromotionMap()
        rawUniqueEquipment1DataMap += DBHelper.get().getUniqueEquipment1Map()
        rawUniqueEquipment2DataMap += DBHelper.get().getUniqueEquipment2Map()
        rawUniqueEquipmentEnhanceData += DBHelper.get().getUniqueEquipmentEnhanceMap()
        rawUnitSkillDataMap  += DBHelper.get().getUnitSkillDataMap()
        rawSkillDataMap += DBHelper.get().getSkillDataCharactersMap()
        rawSkillActionMap += DBHelper.get().getSkillActionCharactersMap()
        rawUnitMinionMap += DBHelper.get().getUnitMinionMap()
        rawUnitAttackPatternMap += DBHelper.get().getUnitAttackPatternCharactersMap()
    }

    fun initClanBattleInfo() {
        rawClanBattlePhaseMap += DBHelper.get().getClanBattlePhaseMap()
        rawWaveGroupClanBattleMap += DBHelper.get().getWaveGroupDataMap()
        rawEnemyClanBattleMap += DBHelper.get().getEnemyClanBattleMap()
        rawUnitAttackPatternMap += DBHelper.get().getUnitAttackPatternClanBattleMap()
        rawSkillDataMap += DBHelper.get().getSkillDataClanBattleMap()
        rawSkillActionMap += DBHelper.get().getSkillActionClanBattleMap()
        rawResistDataMap += DBHelper.get().getResistDataMap()
        rawAilmentMap += DBHelper.get().getAilmentClanBattleMap()
    }

    fun getRawSkillData(skillId: Int) : RawSkillData? {
        return rawSkillDataMap[skillId] ?: DBHelper.get().getSkillData(skillId)
    }

    fun getRawSkillAction(actionId: Int) : RawSkillAction? {
        return rawSkillActionMap[actionId] ?: DBHelper.get().getSkillAction(actionId)
    }

    fun getRawUnitAttackPattern(unitId: Int) : List<RawUnitAttackPattern>? {
        return rawUnitAttackPatternMap[unitId] ?:  DBHelper.get().getUnitAttackPattern(unitId)
    }

    fun getRawEnemy(enemyId: Int) : RawEnemy? {
        return rawEnemyClanBattleMap[enemyId] ?: DBHelper.get().getEnemy(enemyId)
    }

    fun getRawWaveGroup(enemyId: Int) : RawWaveGroup? {
        return rawWaveGroupClanBattleMap[enemyId] ?: DBHelper.get().getWaveGroupData(enemyId)
    }
}
