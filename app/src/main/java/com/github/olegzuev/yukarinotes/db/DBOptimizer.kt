package com.github.olegzuev.yukarinotes.db

import android.annotation.SuppressLint
import android.database.Cursor
import com.github.olegzuev.yukarinotes.utils.FileUtils
import com.github.olegzuev.yukarinotes.utils.LogUtils

object DBOptimizer {

    /************************* extensions **************************/

    private val columnIndexCache: MutableMap<String, Int> = mutableMapOf()

    @SuppressLint("Range")
    fun Cursor.getColumnIndexCache(columnName: String) : Int {
        return columnIndexCache.getOrPut(columnName) { this.getColumnIndex(columnName) }
    }

    @SuppressLint("Range")
    fun Cursor.getInt(columnName: String) : Int {
        return this.getInt(this.getColumnIndexCache(columnName))
    }

    @SuppressLint("Range")
    fun Cursor.getDouble(columnName: String) : Double {
        return this.getDouble(this.getColumnIndexCache(columnName))
    }

    @SuppressLint("Range")
    fun Cursor.getString(columnName: String) : String {
        return this.getString(this.getColumnIndexCache(columnName))
    }

    /************************* methods **************************/

    /***
     * Optimized version of getBeanListByRaw for RawSkillAction
     * @param sql SQL语句
     * @param keyValue 替换？的值
     * @param theClass 类名
     * @param <T> theClass的类
     * @return 生成的实体列表
    </T> */
    @SuppressLint("Recycle")
    @Suppress("UNCHECKED_CAST")
    fun <T> getBeanListByRaw(sql: String?, classSimpleName: String?): List<T>? {
        if (!FileUtils.checkFile(FileUtils.getDbFilePath())) return null
        try {
            columnIndexCache.clear()
            val cursor = DBHelper.get().readableDatabase.rawQuery(sql, null) ?: return null
            return when(classSimpleName) {
                RawUnitRarity::class.simpleName -> cursor2RawUnitRarityList(cursor) as List<T>
                RawCharaStoryStatus::class.simpleName -> cursor2RawCharaStoryStatusList(cursor) as List<T>
                RawPromotionStatus::class.simpleName -> cursor2RawPromotionStatusList(cursor) as List<T>
                RawPromotionBonus::class.simpleName -> cursor2RawPromotionBonusList(cursor) as List<T>
                RawUnitPromotion::class.simpleName -> cursor2RawUnitPromotionList(cursor) as List<T>
                RawUniqueEquipmentData::class.simpleName -> cursor2RawUniqueEquipmentDataList(cursor) as List<T>
                RawUniqueEquipmentEnhanceData::class.simpleName -> cursor2RawUniqueEquipmentEnhanceDataList(cursor) as List<T>
                RawUnitSkillData::class.simpleName -> cursor2RawUnitSkillDataList(cursor) as List<T>
                RawSkillData::class.simpleName -> cursor2RawSkillDataList(cursor) as List<T>
                RawSkillAction::class.simpleName -> cursor2RawSkillActionList(cursor) as List<T>
                RawUnitMinion::class.simpleName -> cursor2RawUnitMinionList(cursor) as List<T>
                RawUnitAttackPattern::class.simpleName -> cursor2RawUnitAttackPatternList(cursor) as List<T>
                RawClanBattlePhase::class.simpleName -> cursor2RawClanBattlePhaseList(cursor) as List<T>
                RawWaveGroup::class.simpleName -> cursor2RawWaveGroupList(cursor) as List<T>
                RawEnemy::class.simpleName -> cursor2RawEnemyList(cursor) as List<T>
                RawResistData::class.simpleName -> cursor2RawResistDataList(cursor) as List<T>
                RawUnitBasic::class.simpleName -> cursor2RawUnitBasicList(cursor) as List<T>
                RawClanBattlePeriod::class.simpleName -> cursor2RawClanBattlePeriodList(cursor) as List<T>
                RawEquipmentData::class.simpleName -> cursor2RawEquipmentDataList(cursor) as List<T>
                RawEquipmentEnhanceData::class.simpleName -> cursor2RawEquipmentEnhanceDataList(cursor) as List<T>
                RawEquipmentPiece::class.simpleName -> cursor2RawEquipmentPieceList(cursor) as List<T>
                else -> throw IllegalAccessException("$classSimpleName is no supported")
            }
        } catch (e: Exception) {
            LogUtils.file(LogUtils.E, "getSkillActionListByRaw", e.message, e.stackTrace)
            return null
        }
    }

    @SuppressLint("Range")
    private fun cursor2RawUnitRarityList(cursor: Cursor): List<RawUnitRarity>? {
        val result: MutableList<RawUnitRarity> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawUnitRarity()
                bean.unit_id = cursor.getInt(RawUnitRarity::unit_id.name)
                bean.rarity = cursor.getInt(RawUnitRarity::rarity.name)

                bean.hp = cursor.getDouble(RawUnitRarity::hp.name)
                bean.atk = cursor.getDouble(RawUnitRarity::atk.name)
                bean.magic_str = cursor.getDouble(RawUnitRarity::magic_str.name)
                bean.def = cursor.getDouble(RawUnitRarity::def.name)
                bean.magic_def = cursor.getDouble(RawUnitRarity::magic_def.name)
                bean.physical_critical = cursor.getDouble(RawUnitRarity::physical_critical.name)
                bean.magic_critical = cursor.getDouble(RawUnitRarity::magic_critical.name)
                bean.wave_hp_recovery = cursor.getDouble(RawUnitRarity::wave_hp_recovery.name)
                bean.wave_energy_recovery = cursor.getDouble(RawUnitRarity::wave_energy_recovery.name)
                bean.dodge = cursor.getDouble(RawUnitRarity::dodge.name)
                bean.physical_penetrate = cursor.getDouble(RawUnitRarity::physical_penetrate.name)
                bean.magic_penetrate = cursor.getDouble(RawUnitRarity::magic_penetrate.name)
                bean.life_steal = cursor.getDouble(RawUnitRarity::life_steal.name)
                bean.hp_recovery_rate = cursor.getDouble(RawUnitRarity::hp_recovery_rate.name)
                bean.energy_recovery_rate = cursor.getDouble(RawUnitRarity::energy_recovery_rate.name)
                bean.energy_reduce_rate = cursor.getDouble(RawUnitRarity::energy_reduce_rate.name)
                bean.accuracy = cursor.getDouble(RawUnitRarity::accuracy.name)

                bean.hp_growth = cursor.getDouble(RawUnitRarity::hp_growth.name)
                bean.atk_growth = cursor.getDouble(RawUnitRarity::atk_growth.name)
                bean.magic_str_growth = cursor.getDouble(RawUnitRarity::magic_str_growth.name)
                bean.def_growth = cursor.getDouble(RawUnitRarity::def_growth.name)
                bean.magic_def_growth = cursor.getDouble(RawUnitRarity::magic_def_growth.name)
                bean.physical_critical_growth = cursor.getDouble(RawUnitRarity::physical_critical_growth.name)
                bean.magic_critical_growth = cursor.getDouble(RawUnitRarity::magic_critical_growth.name)
                bean.wave_hp_recovery_growth = cursor.getDouble(RawUnitRarity::wave_hp_recovery_growth.name)
                bean.wave_energy_recovery_growth = cursor.getDouble(RawUnitRarity::wave_energy_recovery_growth.name)
                bean.dodge_growth = cursor.getDouble(RawUnitRarity::dodge_growth.name)
                bean.physical_penetrate_growth = cursor.getDouble(RawUnitRarity::physical_penetrate_growth.name)
                bean.magic_penetrate_growth = cursor.getDouble(RawUnitRarity::magic_penetrate_growth.name)
                bean.life_steal_growth = cursor.getDouble(RawUnitRarity::life_steal_growth.name)
                bean.hp_recovery_rate_growth = cursor.getDouble(RawUnitRarity::hp_recovery_rate_growth.name)
                bean.energy_recovery_rate_growth = cursor.getDouble(RawUnitRarity::energy_recovery_rate_growth.name)
                bean.energy_reduce_rate_growth = cursor.getDouble(RawUnitRarity::energy_reduce_rate_growth.name)
                bean.accuracy_growth = cursor.getDouble(RawUnitRarity::accuracy_growth.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawCharaStoryStatusList(cursor: Cursor): List<RawCharaStoryStatus>? {
        val result: MutableList<RawCharaStoryStatus> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawCharaStoryStatus()
                bean.story_id = cursor.getInt(RawCharaStoryStatus::story_id.name)
                bean.unlock_story_name = cursor.getString(RawCharaStoryStatus::unlock_story_name.name)
                bean.status_type_1 = cursor.getInt(RawCharaStoryStatus::status_type_1.name)
                bean.status_rate_1 = cursor.getInt(RawCharaStoryStatus::status_rate_1.name)
                bean.status_type_2 = cursor.getInt(RawCharaStoryStatus::status_type_2.name)
                bean.status_rate_2 = cursor.getInt(RawCharaStoryStatus::status_rate_2.name)
                bean.status_type_3 = cursor.getInt(RawCharaStoryStatus::status_type_3.name)
                bean.status_rate_3 = cursor.getInt(RawCharaStoryStatus::status_rate_3.name)
                bean.status_type_4 = cursor.getInt(RawCharaStoryStatus::status_type_4.name)
                bean.status_rate_4 = cursor.getInt(RawCharaStoryStatus::status_rate_4.name)
                bean.status_type_5 = cursor.getInt(RawCharaStoryStatus::status_type_5.name)
                bean.status_rate_5 = cursor.getInt(RawCharaStoryStatus::status_rate_5.name)
                bean.chara_id_1 = cursor.getInt(RawCharaStoryStatus::chara_id_1.name)
                bean.chara_id_2 = cursor.getInt(RawCharaStoryStatus::chara_id_2.name)
                bean.chara_id_3 = cursor.getInt(RawCharaStoryStatus::chara_id_3.name)
                bean.chara_id_4 = cursor.getInt(RawCharaStoryStatus::chara_id_4.name)
                bean.chara_id_5 = cursor.getInt(RawCharaStoryStatus::chara_id_5.name)
                bean.chara_id_6 = cursor.getInt(RawCharaStoryStatus::chara_id_6.name)
                bean.chara_id_7 = cursor.getInt(RawCharaStoryStatus::chara_id_7.name)
                bean.chara_id_8 = cursor.getInt(RawCharaStoryStatus::chara_id_8.name)
                bean.chara_id_9 = cursor.getInt(RawCharaStoryStatus::chara_id_9.name)
                bean.chara_id_10 = cursor.getInt(RawCharaStoryStatus::chara_id_10.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawPromotionStatusList(cursor: Cursor): List<RawPromotionStatus>? {
        val result: MutableList<RawPromotionStatus> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawPromotionStatus()
                bean.unit_id = cursor.getInt(RawPromotionStatus::unit_id.name)
                bean.promotion_level = cursor.getInt(RawPromotionStatus::promotion_level.name)

                bean.hp = cursor.getDouble(RawPromotionStatus::hp.name)
                bean.atk = cursor.getDouble(RawPromotionStatus::atk.name)
                bean.magic_str = cursor.getDouble(RawPromotionStatus::magic_str.name)
                bean.def = cursor.getDouble(RawPromotionStatus::def.name)
                bean.magic_def = cursor.getDouble(RawPromotionStatus::magic_def.name)
                bean.physical_critical = cursor.getDouble(RawPromotionStatus::physical_critical.name)
                bean.magic_critical = cursor.getDouble(RawPromotionStatus::magic_critical.name)
                bean.wave_hp_recovery = cursor.getDouble(RawPromotionStatus::wave_hp_recovery.name)
                bean.wave_energy_recovery = cursor.getDouble(RawPromotionStatus::wave_energy_recovery.name)
                bean.dodge = cursor.getDouble(RawPromotionStatus::dodge.name)
                bean.physical_penetrate = cursor.getDouble(RawPromotionStatus::physical_penetrate.name)
                bean.magic_penetrate = cursor.getDouble(RawPromotionStatus::magic_penetrate.name)
                bean.life_steal = cursor.getDouble(RawPromotionStatus::life_steal.name)
                bean.hp_recovery_rate = cursor.getDouble(RawPromotionStatus::hp_recovery_rate.name)
                bean.energy_recovery_rate = cursor.getDouble(RawPromotionStatus::energy_recovery_rate.name)
                bean.energy_reduce_rate = cursor.getDouble(RawPromotionStatus::energy_reduce_rate.name)
                bean.accuracy = cursor.getDouble(RawPromotionStatus::accuracy.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawPromotionBonusList(cursor: Cursor): List<RawPromotionBonus>? {
        val result: MutableList<RawPromotionBonus> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawPromotionBonus()
                bean.unit_id = cursor.getInt(RawPromotionBonus::unit_id.name)
                bean.promotion_level = cursor.getInt(RawPromotionBonus::promotion_level.name)

                bean.hp = cursor.getDouble(RawPromotionBonus::hp.name)
                bean.atk = cursor.getDouble(RawPromotionBonus::atk.name)
                bean.magic_str = cursor.getDouble(RawPromotionBonus::magic_str.name)
                bean.def = cursor.getDouble(RawPromotionBonus::def.name)
                bean.magic_def = cursor.getDouble(RawPromotionBonus::magic_def.name)
                bean.physical_critical = cursor.getDouble(RawPromotionBonus::physical_critical.name)
                bean.magic_critical = cursor.getDouble(RawPromotionBonus::magic_critical.name)
                bean.wave_hp_recovery = cursor.getDouble(RawPromotionBonus::wave_hp_recovery.name)
                bean.wave_energy_recovery = cursor.getDouble(RawPromotionBonus::wave_energy_recovery.name)
                bean.dodge = cursor.getDouble(RawPromotionBonus::dodge.name)
                bean.physical_penetrate = cursor.getDouble(RawPromotionBonus::physical_penetrate.name)
                bean.magic_penetrate = cursor.getDouble(RawPromotionBonus::magic_penetrate.name)
                bean.life_steal = cursor.getDouble(RawPromotionBonus::life_steal.name)
                bean.hp_recovery_rate = cursor.getDouble(RawPromotionBonus::hp_recovery_rate.name)
                bean.energy_recovery_rate = cursor.getDouble(RawPromotionBonus::energy_recovery_rate.name)
                bean.energy_reduce_rate = cursor.getDouble(RawPromotionBonus::energy_reduce_rate.name)
                bean.accuracy = cursor.getDouble(RawPromotionBonus::accuracy.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawUnitPromotionList(cursor: Cursor): List<RawUnitPromotion>? {
        val result: MutableList<RawUnitPromotion> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawUnitPromotion()
                bean.unit_id = cursor.getInt(RawUnitPromotion::unit_id.name)
                bean.promotion_level = cursor.getInt(RawUnitPromotion::promotion_level.name)
                bean.equip_slot_1 = cursor.getInt(RawUnitPromotion::equip_slot_1.name)
                bean.equip_slot_2 = cursor.getInt(RawUnitPromotion::equip_slot_2.name)
                bean.equip_slot_3 = cursor.getInt(RawUnitPromotion::equip_slot_3.name)
                bean.equip_slot_4 = cursor.getInt(RawUnitPromotion::equip_slot_4.name)
                bean.equip_slot_5 = cursor.getInt(RawUnitPromotion::equip_slot_5.name)
                bean.equip_slot_6 = cursor.getInt(RawUnitPromotion::equip_slot_6.name)
                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawUniqueEquipmentDataList(cursor: Cursor): List<RawUniqueEquipmentData>? {
        val result: MutableList<RawUniqueEquipmentData> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawUniqueEquipmentData()
                bean.unit_id = cursor.getInt(RawUniqueEquipmentData::unit_id.name)
                bean.equipment_id = cursor.getInt(RawUniqueEquipmentData::equipment_id.name)
                bean.equipment_name = cursor.getString(RawUniqueEquipmentData::equipment_name.name)
                bean.description = cursor.getString(RawUniqueEquipmentData::description.name)
                bean.promotion_level = cursor.getInt(RawUniqueEquipmentData::promotion_level.name)
                bean.craft_flg = cursor.getInt(RawUniqueEquipmentData::craft_flg.name)
                bean.equipment_enhance_point = cursor.getInt(RawUniqueEquipmentData::equipment_enhance_point.name)
                bean.sale_price = cursor.getInt(RawUniqueEquipmentData::sale_price.name)
                bean.require_level = cursor.getInt(RawUniqueEquipmentData::require_level.name)
                bean.hp = cursor.getDouble(RawUniqueEquipmentData::hp.name)
                bean.atk = cursor.getDouble(RawUniqueEquipmentData::atk.name)
                bean.magic_str = cursor.getDouble(RawUniqueEquipmentData::magic_str.name)
                bean.def = cursor.getDouble(RawUniqueEquipmentData::def.name)
                bean.magic_def = cursor.getDouble(RawUniqueEquipmentData::magic_def.name)
                bean.physical_critical = cursor.getDouble(RawUniqueEquipmentData::physical_critical.name)
                bean.magic_critical = cursor.getDouble(RawUniqueEquipmentData::magic_critical.name)
                bean.wave_hp_recovery = cursor.getDouble(RawUniqueEquipmentData::wave_hp_recovery.name)
                bean.wave_energy_recovery = cursor.getDouble(RawUniqueEquipmentData::wave_energy_recovery.name)
                bean.dodge = cursor.getDouble(RawUniqueEquipmentData::dodge.name)
                bean.physical_penetrate = cursor.getDouble(RawUniqueEquipmentData::physical_penetrate.name)
                bean.magic_penetrate = cursor.getDouble(RawUniqueEquipmentData::magic_penetrate.name)
                bean.life_steal = cursor.getDouble(RawUniqueEquipmentData::life_steal.name)
                bean.hp_recovery_rate = cursor.getDouble(RawUniqueEquipmentData::hp_recovery_rate.name)
                bean.energy_recovery_rate = cursor.getDouble(RawUniqueEquipmentData::energy_recovery_rate.name)
                bean.energy_reduce_rate = cursor.getDouble(RawUniqueEquipmentData::energy_reduce_rate.name)
                bean.accuracy = cursor.getDouble(RawUniqueEquipmentData::accuracy.name)
                bean.item_id_1 = cursor.getInt(RawUniqueEquipmentData::item_id_1.name)
                bean.consume_num_1 = cursor.getInt(RawUniqueEquipmentData::consume_num_1.name)
                bean.item_id_2 = cursor.getInt(RawUniqueEquipmentData::item_id_2.name)
                bean.consume_num_2 = cursor.getInt(RawUniqueEquipmentData::consume_num_2.name)
                bean.item_id_3 = cursor.getInt(RawUniqueEquipmentData::item_id_3.name)
                bean.consume_num_3 = cursor.getInt(RawUniqueEquipmentData::consume_num_3.name)
                bean.item_id_4 = cursor.getInt(RawUniqueEquipmentData::item_id_4.name)
                bean.consume_num_4 = cursor.getInt(RawUniqueEquipmentData::consume_num_4.name)
                bean.item_id_5 = cursor.getInt(RawUniqueEquipmentData::item_id_5.name)
                bean.consume_num_5 = cursor.getInt(RawUniqueEquipmentData::consume_num_5.name)
                bean.item_id_6 = cursor.getInt(RawUniqueEquipmentData::item_id_6.name)
                bean.consume_num_6 = cursor.getInt(RawUniqueEquipmentData::consume_num_6.name)
                bean.item_id_7 = cursor.getInt(RawUniqueEquipmentData::item_id_7.name)
                bean.consume_num_7 = cursor.getInt(RawUniqueEquipmentData::consume_num_7.name)
                bean.item_id_8 = cursor.getInt(RawUniqueEquipmentData::item_id_8.name)
                bean.consume_num_8 = cursor.getInt(RawUniqueEquipmentData::consume_num_8.name)
                bean.item_id_9 = cursor.getInt(RawUniqueEquipmentData::item_id_9.name)
                bean.consume_num_9 = cursor.getInt(RawUniqueEquipmentData::consume_num_9.name)
                bean.item_id_10 = cursor.getInt(RawUniqueEquipmentData::item_id_10.name)
                bean.consume_num_10 = cursor.getInt(RawUniqueEquipmentData::consume_num_10.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawUniqueEquipmentEnhanceDataList(cursor: Cursor): List<RawUniqueEquipmentEnhanceData>? {
        val result: MutableList<RawUniqueEquipmentEnhanceData> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawUniqueEquipmentEnhanceData()
                bean.equipment_id = cursor.getInt(RawUniqueEquipmentEnhanceData::equipment_id.name)
                bean.hp = cursor.getDouble(RawUniqueEquipmentEnhanceData::hp.name)
                bean.atk = cursor.getDouble(RawUniqueEquipmentEnhanceData::atk.name)
                bean.magic_str = cursor.getDouble(RawUniqueEquipmentEnhanceData::magic_str.name)
                bean.def = cursor.getDouble(RawUniqueEquipmentEnhanceData::def.name)
                bean.magic_def = cursor.getDouble(RawUniqueEquipmentEnhanceData::magic_def.name)
                bean.physical_critical = cursor.getDouble(RawUniqueEquipmentEnhanceData::physical_critical.name)
                bean.magic_critical = cursor.getDouble(RawUniqueEquipmentEnhanceData::magic_critical.name)
                bean.wave_hp_recovery = cursor.getDouble(RawUniqueEquipmentEnhanceData::wave_hp_recovery.name)
                bean.wave_energy_recovery = cursor.getDouble(RawUniqueEquipmentEnhanceData::wave_energy_recovery.name)
                bean.dodge = cursor.getDouble(RawUniqueEquipmentEnhanceData::dodge.name)
                bean.physical_penetrate = cursor.getDouble(RawUniqueEquipmentEnhanceData::physical_penetrate.name)
                bean.magic_penetrate = cursor.getDouble(RawUniqueEquipmentEnhanceData::magic_penetrate.name)
                bean.life_steal = cursor.getDouble(RawUniqueEquipmentEnhanceData::life_steal.name)
                bean.hp_recovery_rate = cursor.getDouble(RawUniqueEquipmentEnhanceData::hp_recovery_rate.name)
                bean.energy_recovery_rate = cursor.getDouble(RawUniqueEquipmentEnhanceData::energy_recovery_rate.name)
                bean.energy_reduce_rate = cursor.getDouble(RawUniqueEquipmentEnhanceData::energy_reduce_rate.name)
                bean.accuracy = cursor.getDouble(RawUniqueEquipmentEnhanceData::accuracy.name)
                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawUnitSkillDataList(cursor: Cursor): List<RawUnitSkillData>? {
        val result: MutableList<RawUnitSkillData> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawUnitSkillData()
                bean.unit_id = cursor.getInt(RawUnitSkillData::unit_id.name)
                bean.union_burst = cursor.getInt(RawUnitSkillData::union_burst.name)
                bean.main_skill_1 = cursor.getInt(RawUnitSkillData::main_skill_1.name)
                bean.main_skill_2 = cursor.getInt(RawUnitSkillData::main_skill_2.name)
                bean.main_skill_3 = cursor.getInt(RawUnitSkillData::main_skill_3.name)
                bean.main_skill_4 = cursor.getInt(RawUnitSkillData::main_skill_4.name)
                bean.main_skill_5 = cursor.getInt(RawUnitSkillData::main_skill_5.name)
                bean.main_skill_6 = cursor.getInt(RawUnitSkillData::main_skill_6.name)
                bean.main_skill_7 = cursor.getInt(RawUnitSkillData::main_skill_7.name)
                bean.main_skill_8 = cursor.getInt(RawUnitSkillData::main_skill_8.name)
                bean.main_skill_9 = cursor.getInt(RawUnitSkillData::main_skill_9.name)
                bean.main_skill_10 = cursor.getInt(RawUnitSkillData::main_skill_10.name)
                bean.ex_skill_1 = cursor.getInt(RawUnitSkillData::ex_skill_1.name)
                bean.ex_skill_evolution_1 = cursor.getInt(RawUnitSkillData::ex_skill_evolution_1.name)
                bean.ex_skill_2 = cursor.getInt(RawUnitSkillData::ex_skill_2.name)
                bean.ex_skill_evolution_2 = cursor.getInt(RawUnitSkillData::ex_skill_evolution_2.name)
                bean.ex_skill_3 = cursor.getInt(RawUnitSkillData::ex_skill_3.name)
                bean.ex_skill_evolution_3 = cursor.getInt(RawUnitSkillData::ex_skill_evolution_3.name)
                bean.ex_skill_4 = cursor.getInt(RawUnitSkillData::ex_skill_4.name)
                bean.ex_skill_evolution_4 = cursor.getInt(RawUnitSkillData::ex_skill_evolution_4.name)
                bean.ex_skill_5 = cursor.getInt(RawUnitSkillData::ex_skill_5.name)
                bean.ex_skill_evolution_5 = cursor.getInt(RawUnitSkillData::ex_skill_evolution_5.name)
                bean.sp_skill_1 = cursor.getInt(RawUnitSkillData::sp_skill_1.name)
                bean.sp_skill_2 = cursor.getInt(RawUnitSkillData::sp_skill_2.name)
                bean.sp_skill_3 = cursor.getInt(RawUnitSkillData::sp_skill_3.name)
                bean.sp_skill_4 = cursor.getInt(RawUnitSkillData::sp_skill_4.name)
                bean.sp_skill_5 = cursor.getInt(RawUnitSkillData::sp_skill_5.name)
                bean.union_burst_evolution = cursor.getInt(RawUnitSkillData::union_burst_evolution.name)
                bean.main_skill_evolution_1 = cursor.getInt(RawUnitSkillData::main_skill_evolution_1.name)
                bean.main_skill_evolution_2 = cursor.getInt(RawUnitSkillData::main_skill_evolution_2.name)
                bean.sp_skill_evolution_1 = cursor.getInt(RawUnitSkillData::sp_skill_evolution_1.name)
                bean.sp_skill_evolution_2 = cursor.getInt(RawUnitSkillData::sp_skill_evolution_2.name)
                bean.sp_union_burst = cursor.getInt(RawUnitSkillData::sp_union_burst.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawSkillDataList(cursor: Cursor): List<RawSkillData>? {
        val result: MutableList<RawSkillData> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawSkillData()
                bean.skill_id = cursor.getInt(RawSkillData::skill_id.name)
                bean.name = cursor.getString(RawSkillData::name.name)
                bean.skill_type = cursor.getInt(RawSkillData::skill_type.name)
                bean.skill_area_width = cursor.getInt(RawSkillData::skill_area_width.name)
                bean.skill_cast_time = cursor.getDouble(RawSkillData::skill_cast_time.name)
                bean.boss_ub_cool_time = cursor.getDouble(RawSkillData::boss_ub_cool_time.name)
                bean.action_1 = cursor.getInt(RawSkillData::action_1.name)
                bean.action_2 = cursor.getInt(RawSkillData::action_2.name)
                bean.action_3 = cursor.getInt(RawSkillData::action_3.name)
                bean.action_4 = cursor.getInt(RawSkillData::action_4.name)
                bean.action_5 = cursor.getInt(RawSkillData::action_5.name)
                bean.action_6 = cursor.getInt(RawSkillData::action_6.name)
                bean.action_7 = cursor.getInt(RawSkillData::action_7.name)
                bean.action_8 = cursor.getInt(RawSkillData::action_8.name)
                bean.action_9 = cursor.getInt(RawSkillData::action_9.name)
                bean.action_10 = cursor.getInt(RawSkillData::action_10.name)
                bean.depend_action_1 = cursor.getInt(RawSkillData::depend_action_1.name)
                bean.depend_action_2 = cursor.getInt(RawSkillData::depend_action_2.name)
                bean.depend_action_3 = cursor.getInt(RawSkillData::depend_action_3.name)
                bean.depend_action_4 = cursor.getInt(RawSkillData::depend_action_4.name)
                bean.depend_action_5 = cursor.getInt(RawSkillData::depend_action_5.name)
                bean.depend_action_6 = cursor.getInt(RawSkillData::depend_action_6.name)
                bean.depend_action_7 = cursor.getInt(RawSkillData::depend_action_7.name)
                bean.depend_action_8 = cursor.getInt(RawSkillData::depend_action_8.name)
                bean.depend_action_9 = cursor.getInt(RawSkillData::depend_action_9.name)
                bean.depend_action_10 = cursor.getInt(RawSkillData::depend_action_10.name)
                bean.description = cursor.getString(RawSkillData::description.name)
                bean.icon_type = cursor.getInt(RawSkillData::icon_type.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawSkillActionList(cursor: Cursor): List<RawSkillAction>? {
        val result: MutableList<RawSkillAction> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawSkillAction()
                bean.action_id = cursor.getInt(RawSkillAction::action_id.name)
                bean.class_id = cursor.getInt(RawSkillAction::class_id.name)
                bean.action_type = cursor.getInt(RawSkillAction::action_type.name)
                bean.action_detail_1 = cursor.getInt(RawSkillAction::action_detail_1.name)
                bean.action_detail_2 = cursor.getInt(RawSkillAction::action_detail_2.name)
                bean.action_detail_3 = cursor.getInt(RawSkillAction::action_detail_3.name)
                bean.action_value_1 = cursor.getDouble(RawSkillAction::action_value_1.name)
                bean.action_value_2 = cursor.getDouble(RawSkillAction::action_value_2.name)
                bean.action_value_3 = cursor.getDouble(RawSkillAction::action_value_3.name)
                bean.action_value_4 = cursor.getDouble(RawSkillAction::action_value_4.name)
                bean.action_value_5 = cursor.getDouble(RawSkillAction::action_value_5.name)
                bean.action_value_6 = cursor.getDouble(RawSkillAction::action_value_6.name)
                bean.action_value_7 = cursor.getDouble(RawSkillAction::action_value_7.name)
                bean.target_assignment = cursor.getInt(RawSkillAction::target_assignment.name)
                bean.target_area = cursor.getInt(RawSkillAction::target_area.name)
                bean.target_range = cursor.getInt(RawSkillAction::target_range.name)
                bean.target_type = cursor.getInt(RawSkillAction::target_type.name)
                bean.target_number = cursor.getInt(RawSkillAction::target_number.name)
                bean.target_count = cursor.getInt(RawSkillAction::target_count.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawUnitMinionList(cursor: Cursor): List<RawUnitMinion>? {
        val result: MutableList<RawUnitMinion> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawUnitMinion()
                bean.unit_id = cursor.getInt(RawUnitMinion::unit_id.name)
                bean.unit_name = cursor.getString(RawUnitMinion::unit_name.name)
                bean.prefab_id = cursor.getInt(RawUnitMinion::prefab_id.name)
                bean.move_speed = cursor.getInt(RawUnitMinion::move_speed.name)
                bean.search_area_width = cursor.getInt(RawUnitMinion::search_area_width.name)
                bean.atk_type = cursor.getInt(RawUnitMinion::atk_type.name)
                bean.normal_atk_cast_time = cursor.getDouble(RawUnitMinion::normal_atk_cast_time.name)

                bean.union_burst = cursor.getInt(RawUnitMinion::union_burst.name)
                bean.main_skill_1 = cursor.getInt(RawUnitMinion::main_skill_1.name)
                bean.main_skill_2 = cursor.getInt(RawUnitMinion::main_skill_2.name)
                bean.main_skill_3 = cursor.getInt(RawUnitMinion::main_skill_3.name)
                bean.main_skill_4 = cursor.getInt(RawUnitMinion::main_skill_4.name)
                bean.main_skill_5 = cursor.getInt(RawUnitMinion::main_skill_5.name)
                bean.main_skill_6 = cursor.getInt(RawUnitMinion::main_skill_6.name)
                bean.main_skill_7 = cursor.getInt(RawUnitMinion::main_skill_7.name)
                bean.main_skill_8 = cursor.getInt(RawUnitMinion::main_skill_8.name)
                bean.main_skill_9 = cursor.getInt(RawUnitMinion::main_skill_9.name)
                bean.main_skill_10 = cursor.getInt(RawUnitMinion::main_skill_10.name)
                bean.ex_skill_1 = cursor.getInt(RawUnitMinion::ex_skill_1.name)
                bean.ex_skill_evolution_1 = cursor.getInt(RawUnitMinion::ex_skill_evolution_1.name)
                bean.ex_skill_2 = cursor.getInt(RawUnitMinion::ex_skill_2.name)
                bean.ex_skill_evolution_2 = cursor.getInt(RawUnitMinion::ex_skill_evolution_2.name)
                bean.ex_skill_3 = cursor.getInt(RawUnitMinion::ex_skill_3.name)
                bean.ex_skill_evolution_3 = cursor.getInt(RawUnitMinion::ex_skill_evolution_3.name)
                bean.ex_skill_4 = cursor.getInt(RawUnitMinion::ex_skill_4.name)
                bean.ex_skill_evolution_4 = cursor.getInt(RawUnitMinion::ex_skill_evolution_4.name)
                bean.ex_skill_5 = cursor.getInt(RawUnitMinion::ex_skill_5.name)
                bean.ex_skill_evolution_5 = cursor.getInt(RawUnitMinion::ex_skill_evolution_5.name)
                bean.sp_skill_1 = cursor.getInt(RawUnitMinion::sp_skill_1.name)
                bean.sp_skill_2 = cursor.getInt(RawUnitMinion::sp_skill_2.name)
                bean.sp_skill_3 = cursor.getInt(RawUnitMinion::sp_skill_3.name)
                bean.sp_skill_4 = cursor.getInt(RawUnitMinion::sp_skill_4.name)
                bean.sp_skill_5 = cursor.getInt(RawUnitMinion::sp_skill_5.name)
                bean.union_burst_evolution = cursor.getInt(RawUnitMinion::union_burst_evolution.name)
                bean.main_skill_evolution_1 = cursor.getInt(RawUnitMinion::main_skill_evolution_1.name)
                bean.main_skill_evolution_2 = cursor.getInt(RawUnitMinion::main_skill_evolution_2.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawUnitAttackPatternList(cursor: Cursor): List<RawUnitAttackPattern>? {
        val result: MutableList<RawUnitAttackPattern> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawUnitAttackPattern()
                bean.pattern_id = cursor.getInt(RawUnitAttackPattern::pattern_id.name)
                bean.unit_id = cursor.getInt(RawUnitAttackPattern::unit_id.name)
                bean.loop_start = cursor.getInt(RawUnitAttackPattern::loop_start.name)
                bean.loop_end = cursor.getInt(RawUnitAttackPattern::loop_end.name)
                bean.atk_pattern_1 = cursor.getInt(RawUnitAttackPattern::atk_pattern_1.name)
                bean.atk_pattern_2 = cursor.getInt(RawUnitAttackPattern::atk_pattern_2.name)
                bean.atk_pattern_3 = cursor.getInt(RawUnitAttackPattern::atk_pattern_3.name)
                bean.atk_pattern_4 = cursor.getInt(RawUnitAttackPattern::atk_pattern_4.name)
                bean.atk_pattern_5 = cursor.getInt(RawUnitAttackPattern::atk_pattern_5.name)
                bean.atk_pattern_6 = cursor.getInt(RawUnitAttackPattern::atk_pattern_6.name)
                bean.atk_pattern_7 = cursor.getInt(RawUnitAttackPattern::atk_pattern_7.name)
                bean.atk_pattern_8 = cursor.getInt(RawUnitAttackPattern::atk_pattern_8.name)
                bean.atk_pattern_9 = cursor.getInt(RawUnitAttackPattern::atk_pattern_9.name)
                bean.atk_pattern_10 = cursor.getInt(RawUnitAttackPattern::atk_pattern_10.name)
                bean.atk_pattern_11 = cursor.getInt(RawUnitAttackPattern::atk_pattern_11.name)
                bean.atk_pattern_12 = cursor.getInt(RawUnitAttackPattern::atk_pattern_12.name)
                bean.atk_pattern_13 = cursor.getInt(RawUnitAttackPattern::atk_pattern_13.name)
                bean.atk_pattern_14 = cursor.getInt(RawUnitAttackPattern::atk_pattern_14.name)
                bean.atk_pattern_15 = cursor.getInt(RawUnitAttackPattern::atk_pattern_15.name)
                bean.atk_pattern_16 = cursor.getInt(RawUnitAttackPattern::atk_pattern_16.name)
                bean.atk_pattern_17 = cursor.getInt(RawUnitAttackPattern::atk_pattern_17.name)
                bean.atk_pattern_18 = cursor.getInt(RawUnitAttackPattern::atk_pattern_18.name)
                bean.atk_pattern_19 = cursor.getInt(RawUnitAttackPattern::atk_pattern_19.name)
                bean.atk_pattern_20 = cursor.getInt(RawUnitAttackPattern::atk_pattern_20.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawClanBattlePhaseList(cursor: Cursor): List<RawClanBattlePhase>? {
        val result: MutableList<RawClanBattlePhase> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawClanBattlePhase()
                bean.clan_battle_id = cursor.getInt(RawClanBattlePhase::clan_battle_id.name)
                bean.phase = cursor.getInt(RawClanBattlePhase::phase.name)
                bean.wave_group_id_1 = cursor.getInt(RawClanBattlePhase::wave_group_id_1.name)
                bean.wave_group_id_2 = cursor.getInt(RawClanBattlePhase::wave_group_id_2.name)
                bean.wave_group_id_3 = cursor.getInt(RawClanBattlePhase::wave_group_id_3.name)
                bean.wave_group_id_4 = cursor.getInt(RawClanBattlePhase::wave_group_id_4.name)
                bean.wave_group_id_5 = cursor.getInt(RawClanBattlePhase::wave_group_id_5.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawWaveGroupList(cursor: Cursor): List<RawWaveGroup>? {
        val result: MutableList<RawWaveGroup> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawWaveGroup()
                bean.id = cursor.getInt(RawWaveGroup::id.name)
                bean.wave_group_id = cursor.getInt(RawWaveGroup::wave_group_id.name)
                bean.enemy_id_1 = cursor.getInt(RawWaveGroup::enemy_id_1.name)
                bean.drop_gold_1 = cursor.getInt(RawWaveGroup::drop_gold_1.name)
                bean.drop_reward_id_1 = cursor.getInt(RawWaveGroup::drop_reward_id_1.name)
                bean.enemy_id_2 = cursor.getInt(RawWaveGroup::enemy_id_2.name)
                bean.drop_gold_2 = cursor.getInt(RawWaveGroup::drop_gold_2.name)
                bean.drop_reward_id_2 = cursor.getInt(RawWaveGroup::drop_reward_id_2.name)
                bean.enemy_id_3 = cursor.getInt(RawWaveGroup::enemy_id_3.name)
                bean.drop_gold_3 = cursor.getInt(RawWaveGroup::drop_gold_3.name)
                bean.drop_reward_id_3 = cursor.getInt(RawWaveGroup::drop_reward_id_3.name)
                bean.enemy_id_4 = cursor.getInt(RawWaveGroup::enemy_id_4.name)
                bean.drop_gold_4 = cursor.getInt(RawWaveGroup::drop_gold_4.name)
                bean.drop_reward_id_4 = cursor.getInt(RawWaveGroup::drop_reward_id_4.name)
                bean.enemy_id_5 = cursor.getInt(RawWaveGroup::enemy_id_5.name)
                bean.drop_gold_5 = cursor.getInt(RawWaveGroup::drop_gold_5.name)
                bean.drop_reward_id_5 = cursor.getInt(RawWaveGroup::drop_reward_id_5.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawEnemyList(cursor: Cursor): List<RawEnemy>? {
        val result: MutableList<RawEnemy> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawEnemy()
                bean.enemy_id = cursor.getInt(RawEnemy::enemy_id.name)
                bean.unit_id = cursor.getInt(RawEnemy::unit_id.name)
                bean.name = cursor.getString(RawEnemy::name.name)
                bean.level = cursor.getInt(RawEnemy::level.name)
                bean.resist_status_id = cursor.getInt(RawEnemy::resist_status_id.name)
                bean.prefab_id = cursor.getInt(RawEnemy::prefab_id.name)
                bean.atk_type = cursor.getInt(RawEnemy::atk_type.name)
                bean.search_area_width = cursor.getInt(RawEnemy::search_area_width.name)
                bean.normal_atk_cast_time = cursor.getDouble(RawEnemy::normal_atk_cast_time.name)
                bean.comment = cursor.getString(RawEnemy::comment.name)

                bean.hp = cursor.getInt(RawEnemy::hp.name)
                bean.atk = cursor.getInt(RawEnemy::atk.name)
                bean.magic_str = cursor.getInt(RawEnemy::magic_str.name)
                bean.def = cursor.getInt(RawEnemy::def.name)
                bean.magic_def = cursor.getInt(RawEnemy::magic_def.name)
                bean.physical_critical = cursor.getInt(RawEnemy::physical_critical.name)
                bean.magic_critical = cursor.getInt(RawEnemy::magic_critical.name)
                bean.wave_hp_recovery = cursor.getInt(RawEnemy::wave_hp_recovery.name)
                bean.wave_energy_Recovery = cursor.getInt(RawEnemy::wave_energy_Recovery.name)
                bean.dodge = cursor.getInt(RawEnemy::dodge.name)
                bean.physical_penetrate = cursor.getInt(RawEnemy::physical_penetrate.name)
                bean.magic_penetrate = cursor.getInt(RawEnemy::magic_penetrate.name)
                bean.life_steal = cursor.getInt(RawEnemy::life_steal.name)
                bean.hp_recovery_rate = cursor.getInt(RawEnemy::hp_recovery_rate.name)
                bean.energy_recovery_rate = cursor.getInt(RawEnemy::energy_recovery_rate.name)
                bean.energy_reduce_rate = cursor.getInt(RawEnemy::energy_reduce_rate.name)
                bean.accuracy = cursor.getInt(RawEnemy::accuracy.name)
                bean.union_burst_level = cursor.getInt(RawEnemy::union_burst_level.name)
                bean.main_skill_lv_1 = cursor.getInt(RawEnemy::main_skill_lv_1.name)
                bean.main_skill_lv_2 = cursor.getInt(RawEnemy::main_skill_lv_2.name)
                bean.main_skill_lv_3 = cursor.getInt(RawEnemy::main_skill_lv_3.name)
                bean.main_skill_lv_4 = cursor.getInt(RawEnemy::main_skill_lv_4.name)
                bean.main_skill_lv_5 = cursor.getInt(RawEnemy::main_skill_lv_5.name)
                bean.main_skill_lv_6 = cursor.getInt(RawEnemy::main_skill_lv_6.name)
                bean.main_skill_lv_7 = cursor.getInt(RawEnemy::main_skill_lv_7.name)
                bean.main_skill_lv_8 = cursor.getInt(RawEnemy::main_skill_lv_8.name)
                bean.main_skill_lv_9 = cursor.getInt(RawEnemy::main_skill_lv_9.name)
                bean.main_skill_lv_10 = cursor.getInt(RawEnemy::main_skill_lv_10.name)
                bean.ex_skill_lv_1 = cursor.getInt(RawEnemy::ex_skill_lv_1.name)
                bean.ex_skill_lv_2 = cursor.getInt(RawEnemy::ex_skill_lv_2.name)
                bean.ex_skill_lv_3 = cursor.getInt(RawEnemy::ex_skill_lv_3.name)
                bean.ex_skill_lv_4 = cursor.getInt(RawEnemy::ex_skill_lv_4.name)
                bean.ex_skill_lv_5 = cursor.getInt(RawEnemy::ex_skill_lv_5.name)
                bean.child_enemy_parameter_1 = cursor.getInt(RawEnemy::child_enemy_parameter_1.name)
                bean.child_enemy_parameter_2 = cursor.getInt(RawEnemy::child_enemy_parameter_2.name)
                bean.child_enemy_parameter_3 = cursor.getInt(RawEnemy::child_enemy_parameter_3.name)
                bean.child_enemy_parameter_4 = cursor.getInt(RawEnemy::child_enemy_parameter_4.name)
                bean.child_enemy_parameter_5 = cursor.getInt(RawEnemy::child_enemy_parameter_5.name)

                bean.union_burst = cursor.getInt(RawEnemy::union_burst.name)
                bean.main_skill_1 = cursor.getInt(RawEnemy::main_skill_1.name)
                bean.main_skill_2 = cursor.getInt(RawEnemy::main_skill_2.name)
                bean.main_skill_3 = cursor.getInt(RawEnemy::main_skill_3.name)
                bean.main_skill_4 = cursor.getInt(RawEnemy::main_skill_4.name)
                bean.main_skill_5 = cursor.getInt(RawEnemy::main_skill_5.name)
                bean.main_skill_6 = cursor.getInt(RawEnemy::main_skill_6.name)
                bean.main_skill_7 = cursor.getInt(RawEnemy::main_skill_7.name)
                bean.main_skill_8 = cursor.getInt(RawEnemy::main_skill_8.name)
                bean.main_skill_9 = cursor.getInt(RawEnemy::main_skill_9.name)
                bean.main_skill_10 = cursor.getInt(RawEnemy::main_skill_10.name)
                bean.ex_skill_1 = cursor.getInt(RawEnemy::ex_skill_1.name)
                bean.ex_skill_evolution_1 = cursor.getInt(RawEnemy::ex_skill_evolution_1.name)
                bean.ex_skill_2 = cursor.getInt(RawEnemy::ex_skill_2.name)
                bean.ex_skill_evolution_2 = cursor.getInt(RawEnemy::ex_skill_evolution_2.name)
                bean.ex_skill_3 = cursor.getInt(RawEnemy::ex_skill_3.name)
                bean.ex_skill_evolution_3 = cursor.getInt(RawEnemy::ex_skill_evolution_3.name)
                bean.ex_skill_4 = cursor.getInt(RawEnemy::ex_skill_4.name)
                bean.ex_skill_evolution_4 = cursor.getInt(RawEnemy::ex_skill_evolution_4.name)
                bean.ex_skill_5 = cursor.getInt(RawEnemy::ex_skill_5.name)
                bean.ex_skill_evolution_5 = cursor.getInt(RawEnemy::ex_skill_evolution_5.name)
                bean.sp_skill_1 = cursor.getInt(RawEnemy::sp_skill_1.name)
                bean.sp_skill_2 = cursor.getInt(RawEnemy::sp_skill_2.name)
                bean.sp_skill_3 = cursor.getInt(RawEnemy::sp_skill_3.name)
                bean.sp_skill_4 = cursor.getInt(RawEnemy::sp_skill_4.name)
                bean.sp_skill_5 = cursor.getInt(RawEnemy::sp_skill_5.name)
                bean.union_burst_evolution = cursor.getInt(RawEnemy::union_burst_evolution.name)
                bean.main_skill_evolution_1 = cursor.getInt(RawEnemy::main_skill_evolution_1.name)
                bean.main_skill_evolution_2 = cursor.getInt(RawEnemy::main_skill_evolution_2.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawResistDataList(cursor: Cursor): List<RawResistData>? {
        val result: MutableList<RawResistData> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawResistData()
                bean.resist_status_id = cursor.getInt(RawResistData::resist_status_id.name)
                bean.ailment_1 = cursor.getInt(RawResistData::ailment_1.name)
                bean.ailment_2 = cursor.getInt(RawResistData::ailment_2.name)
                bean.ailment_3 = cursor.getInt(RawResistData::ailment_3.name)
                bean.ailment_4 = cursor.getInt(RawResistData::ailment_4.name)
                bean.ailment_5 = cursor.getInt(RawResistData::ailment_5.name)
                bean.ailment_6 = cursor.getInt(RawResistData::ailment_6.name)
                bean.ailment_7 = cursor.getInt(RawResistData::ailment_7.name)
                bean.ailment_8 = cursor.getInt(RawResistData::ailment_8.name)
                bean.ailment_9 = cursor.getInt(RawResistData::ailment_9.name)
                bean.ailment_10 = cursor.getInt(RawResistData::ailment_10.name)
                bean.ailment_11 = cursor.getInt(RawResistData::ailment_11.name)
                bean.ailment_12 = cursor.getInt(RawResistData::ailment_12.name)
                bean.ailment_13 = cursor.getInt(RawResistData::ailment_13.name)
                bean.ailment_14 = cursor.getInt(RawResistData::ailment_14.name)
                bean.ailment_15 = cursor.getInt(RawResistData::ailment_15.name)
                bean.ailment_16 = cursor.getInt(RawResistData::ailment_16.name)
                bean.ailment_17 = cursor.getInt(RawResistData::ailment_17.name)
                bean.ailment_18 = cursor.getInt(RawResistData::ailment_18.name)
                bean.ailment_19 = cursor.getInt(RawResistData::ailment_19.name)
                bean.ailment_20 = cursor.getInt(RawResistData::ailment_20.name)
                bean.ailment_21 = cursor.getInt(RawResistData::ailment_21.name)
                bean.ailment_22 = cursor.getInt(RawResistData::ailment_22.name)
                bean.ailment_23 = cursor.getInt(RawResistData::ailment_23.name)
                bean.ailment_24 = cursor.getInt(RawResistData::ailment_24.name)
                bean.ailment_25 = cursor.getInt(RawResistData::ailment_25.name)
                bean.ailment_26 = cursor.getInt(RawResistData::ailment_26.name)
                bean.ailment_27 = cursor.getInt(RawResistData::ailment_27.name)
                bean.ailment_28 = cursor.getInt(RawResistData::ailment_28.name)
                bean.ailment_29 = cursor.getInt(RawResistData::ailment_29.name)
                bean.ailment_30 = cursor.getInt(RawResistData::ailment_30.name)
                bean.ailment_31 = cursor.getInt(RawResistData::ailment_31.name)
                bean.ailment_32 = cursor.getInt(RawResistData::ailment_32.name)
                bean.ailment_33 = cursor.getInt(RawResistData::ailment_33.name)
                bean.ailment_34 = cursor.getInt(RawResistData::ailment_34.name)
                bean.ailment_35 = cursor.getInt(RawResistData::ailment_35.name)
                bean.ailment_36 = cursor.getInt(RawResistData::ailment_36.name)
                bean.ailment_37 = cursor.getInt(RawResistData::ailment_37.name)
                bean.ailment_38 = cursor.getInt(RawResistData::ailment_38.name)
                bean.ailment_39 = cursor.getInt(RawResistData::ailment_39.name)
                bean.ailment_40 = cursor.getInt(RawResistData::ailment_40.name)
                bean.ailment_41 = cursor.getInt(RawResistData::ailment_41.name)
                bean.ailment_42 = cursor.getInt(RawResistData::ailment_42.name)
                bean.ailment_43 = cursor.getInt(RawResistData::ailment_43.name)
                bean.ailment_44 = cursor.getInt(RawResistData::ailment_44.name)
                bean.ailment_45 = cursor.getInt(RawResistData::ailment_45.name)
                bean.ailment_46 = cursor.getInt(RawResistData::ailment_46.name)
                bean.ailment_47 = cursor.getInt(RawResistData::ailment_47.name)
                bean.ailment_48 = cursor.getInt(RawResistData::ailment_48.name)
                bean.ailment_49 = cursor.getInt(RawResistData::ailment_49.name)
                bean.ailment_50 = cursor.getInt(RawResistData::ailment_50.name)
                bean.ailment_51 = cursor.getInt(RawResistData::ailment_51.name)
                bean.ailment_52 = cursor.getInt(RawResistData::ailment_52.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawUnitBasicList(cursor: Cursor): List<RawUnitBasic>? {
        val result: MutableList<RawUnitBasic> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawUnitBasic()
                bean.unit_id = cursor.getInt(RawUnitBasic::unit_id.name)
                bean.unit_conversion_id = cursor.getInt(RawUnitBasic::unit_conversion_id.name)
                bean.unit_name = cursor.getString(RawUnitBasic::unit_name.name)
                bean.prefab_id = cursor.getInt(RawUnitBasic::prefab_id.name)
                bean.move_speed = cursor.getInt(RawUnitBasic::move_speed.name)
                bean.search_area_width = cursor.getInt(RawUnitBasic::search_area_width.name)
                bean.atk_type = cursor.getInt(RawUnitBasic::atk_type.name)
                bean.normal_atk_cast_time = cursor.getDouble(RawUnitBasic::normal_atk_cast_time.name)
                bean.guild_id = cursor.getInt(RawUnitBasic::guild_id.name)
                bean.comment = cursor.getString(RawUnitBasic::comment.name)
                bean.start_time = cursor.getString(RawUnitBasic::start_time.name)
                bean.age = cursor.getString(RawUnitBasic::age.name)
                bean.guild = cursor.getString(RawUnitBasic::guild.name)
                bean.race = cursor.getString(RawUnitBasic::race.name)
                bean.height = cursor.getString(RawUnitBasic::height.name)
                bean.weight = cursor.getString(RawUnitBasic::weight.name)
                bean.birth_month = cursor.getString(RawUnitBasic::birth_month.name)
                bean.birth_day = cursor.getString(RawUnitBasic::birth_day.name)
                bean.blood_type = cursor.getString(RawUnitBasic::blood_type.name)
                bean.favorite = cursor.getString(RawUnitBasic::favorite.name)
                bean.voice = cursor.getString(RawUnitBasic::voice.name)
                bean.catch_copy = cursor.getString(RawUnitBasic::catch_copy.name)
                bean.self_text = cursor.getString(RawUnitBasic::self_text.name)
                bean.actual_name = cursor.getString(RawUnitBasic::actual_name.name)
                bean.kana = cursor.getString(RawUnitBasic::kana.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawClanBattlePeriodList(cursor: Cursor): List<RawClanBattlePeriod>? {
        val result: MutableList<RawClanBattlePeriod> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawClanBattlePeriod()
                bean.clan_battle_id = cursor.getInt(RawClanBattlePeriod::clan_battle_id.name)
                bean.start_time = cursor.getString(RawClanBattlePeriod::start_time.name)
                bean.end_time = cursor.getString(RawClanBattlePeriod::end_time.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawEquipmentDataList(cursor: Cursor): List<RawEquipmentData>? {
        val result: MutableList<RawEquipmentData> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawEquipmentData()
                bean.equipment_id = cursor.getInt(RawEquipmentData::equipment_id.name)
                bean.equipment_name = cursor.getString(RawEquipmentData::equipment_name.name)
                bean.description = cursor.getString(RawEquipmentData::description.name)
                bean.promotion_level = cursor.getInt(RawEquipmentData::promotion_level.name)
                bean.craft_flg = cursor.getInt(RawEquipmentData::craft_flg.name)
                bean.equipment_enhance_point = cursor.getInt(RawEquipmentData::equipment_enhance_point.name)
                bean.sale_price = cursor.getInt(RawEquipmentData::sale_price.name)
                bean.require_level = cursor.getInt(RawEquipmentData::require_level.name)
                bean.max_equipment_enhance_level = cursor.getInt(RawEquipmentData::max_equipment_enhance_level.name)
                bean.hp = cursor.getDouble(RawEquipmentData::hp.name)
                bean.atk = cursor.getDouble(RawEquipmentData::atk.name)
                bean.magic_str = cursor.getDouble(RawEquipmentData::magic_str.name)
                bean.def = cursor.getDouble(RawEquipmentData::def.name)
                bean.magic_def = cursor.getDouble(RawEquipmentData::magic_def.name)
                bean.physical_critical = cursor.getDouble(RawEquipmentData::physical_critical.name)
                bean.magic_critical = cursor.getDouble(RawEquipmentData::magic_critical.name)
                bean.wave_hp_recovery = cursor.getDouble(RawEquipmentData::wave_hp_recovery.name)
                bean.wave_energy_recovery = cursor.getDouble(RawEquipmentData::wave_energy_recovery.name)
                bean.dodge = cursor.getDouble(RawEquipmentData::dodge.name)
                bean.physical_penetrate = cursor.getDouble(RawEquipmentData::physical_penetrate.name)
                bean.magic_penetrate = cursor.getDouble(RawEquipmentData::magic_penetrate.name)
                bean.life_steal = cursor.getDouble(RawEquipmentData::life_steal.name)
                bean.hp_recovery_rate = cursor.getDouble(RawEquipmentData::hp_recovery_rate.name)
                bean.energy_recovery_rate = cursor.getDouble(RawEquipmentData::energy_recovery_rate.name)
                bean.energy_reduce_rate = cursor.getDouble(RawEquipmentData::energy_reduce_rate.name)
                bean.accuracy = cursor.getDouble(RawEquipmentData::accuracy.name)
                bean.catalog = cursor.getString(RawEquipmentData::catalog.name)
                bean.rarity = cursor.getInt(RawEquipmentData::rarity.name)
                bean.condition_equipment_id_1 = cursor.getInt(RawEquipmentData::condition_equipment_id_1.name)
                bean.consume_num_1 = cursor.getInt(RawEquipmentData::consume_num_1.name)
                bean.condition_equipment_id_2 = cursor.getInt(RawEquipmentData::condition_equipment_id_2.name)
                bean.consume_num_2 = cursor.getInt(RawEquipmentData::consume_num_2.name)
                bean.condition_equipment_id_3 = cursor.getInt(RawEquipmentData::condition_equipment_id_3.name)
                bean.consume_num_3 = cursor.getInt(RawEquipmentData::consume_num_3.name)
                bean.condition_equipment_id_4 = cursor.getInt(RawEquipmentData::condition_equipment_id_4.name)
                bean.consume_num_4 = cursor.getInt(RawEquipmentData::consume_num_4.name)
                bean.condition_equipment_id_5 = cursor.getInt(RawEquipmentData::condition_equipment_id_5.name)
                bean.consume_num_5 = cursor.getInt(RawEquipmentData::consume_num_5.name)
                bean.condition_equipment_id_6 = cursor.getInt(RawEquipmentData::condition_equipment_id_6.name)
                bean.consume_num_6 = cursor.getInt(RawEquipmentData::consume_num_6.name)
                bean.condition_equipment_id_7 = cursor.getInt(RawEquipmentData::condition_equipment_id_7.name)
                bean.consume_num_7 = cursor.getInt(RawEquipmentData::consume_num_7.name)
                bean.condition_equipment_id_8 = cursor.getInt(RawEquipmentData::condition_equipment_id_8.name)
                bean.consume_num_8 = cursor.getInt(RawEquipmentData::consume_num_8.name)
                bean.condition_equipment_id_9 = cursor.getInt(RawEquipmentData::condition_equipment_id_9.name)
                bean.consume_num_9 = cursor.getInt(RawEquipmentData::consume_num_9.name)
                bean.condition_equipment_id_10 = cursor.getInt(RawEquipmentData::condition_equipment_id_10.name)
                bean.consume_num_10 = cursor.getInt(RawEquipmentData::consume_num_10.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawEquipmentEnhanceDataList(cursor: Cursor): List<RawEquipmentEnhanceData>? {
        val result: MutableList<RawEquipmentEnhanceData> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawEquipmentEnhanceData()
                bean.equipment_id = cursor.getInt(RawEquipmentEnhanceData::equipment_id.name)

                bean.hp = cursor.getDouble(RawEquipmentEnhanceData::hp.name)
                bean.atk = cursor.getDouble(RawEquipmentEnhanceData::atk.name)
                bean.magic_str = cursor.getDouble(RawEquipmentEnhanceData::magic_str.name)
                bean.def = cursor.getDouble(RawEquipmentEnhanceData::def.name)
                bean.magic_def = cursor.getDouble(RawEquipmentEnhanceData::magic_def.name)
                bean.physical_critical = cursor.getDouble(RawEquipmentEnhanceData::physical_critical.name)
                bean.magic_critical = cursor.getDouble(RawEquipmentEnhanceData::magic_critical.name)
                bean.wave_hp_recovery = cursor.getDouble(RawEquipmentEnhanceData::wave_hp_recovery.name)
                bean.wave_energy_recovery = cursor.getDouble(RawEquipmentEnhanceData::wave_energy_recovery.name)
                bean.dodge = cursor.getDouble(RawEquipmentEnhanceData::dodge.name)
                bean.physical_penetrate = cursor.getDouble(RawEquipmentEnhanceData::physical_penetrate.name)
                bean.magic_penetrate = cursor.getDouble(RawEquipmentEnhanceData::magic_penetrate.name)
                bean.life_steal = cursor.getDouble(RawEquipmentEnhanceData::life_steal.name)
                bean.hp_recovery_rate = cursor.getDouble(RawEquipmentEnhanceData::hp_recovery_rate.name)
                bean.energy_recovery_rate = cursor.getDouble(RawEquipmentEnhanceData::energy_recovery_rate.name)
                bean.energy_reduce_rate = cursor.getDouble(RawEquipmentEnhanceData::energy_reduce_rate.name)
                bean.accuracy = cursor.getDouble(RawEquipmentEnhanceData::accuracy.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }

    @SuppressLint("Range")
    private fun cursor2RawEquipmentPieceList(cursor: Cursor): List<RawEquipmentPiece>? {
        val result: MutableList<RawEquipmentPiece> = mutableListOf()
        try {
            while (cursor.moveToNext()) {
                if (cursor.isBeforeFirst) {
                    continue
                }
                val bean = RawEquipmentPiece()
                bean.equipment_id = cursor.getInt(RawEquipmentPiece::equipment_id.name)
                bean.equipment_name = cursor.getString(RawEquipmentPiece::equipment_name.name)

                result.add(bean)
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } finally {
            cursor.close()
        }
        return result
    }
}