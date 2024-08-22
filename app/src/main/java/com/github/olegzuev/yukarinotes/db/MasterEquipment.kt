package com.github.olegzuev.yukarinotes.db

import com.github.olegzuev.yukarinotes.data.Equipment
import com.github.olegzuev.yukarinotes.data.EquipmentPiece
import com.github.olegzuev.yukarinotes.data.Item
import com.github.olegzuev.yukarinotes.data.Property

class MasterEquipment {
    fun getEquipmentMap(): MutableMap<Int, Equipment> {
        val equipmentMap = mutableMapOf<Int, Equipment>()
        val rawEquipmentList = DBHelper.get().getEquipmentAll()
        val rawEquipmentEnhanceList = DBHelper.get().getEquipmentEnhance()

        val equipmentPieceMap = mutableMapOf<Int, EquipmentPiece>()
        val rawEquipmentPieceList = DBHelper.get().getEquipmentPiece()

        rawEquipmentPieceList?.forEach {
            equipmentPieceMap[it.equipment_id] = it.equipmentPiece
        }

        rawEquipmentList?.forEach {
            val enhanceProperty =
                rawEquipmentEnhanceList?.find { enhance -> enhance.equipment_id == it.equipment_id }?.property
                    ?: Property()

            equipmentMap[it.equipment_id] = Equipment(
                it.equipment_id,
                it.equipment_name,
                it.description.replace("\\n", ""),
                it.promotion_level,
                it.craft_flg,
                it.equipment_enhance_point,
                it.sale_price,
                it.require_level,
                it.max_equipment_enhance_level,
                it.property,
                listOf(enhanceProperty),
                it.catalog,
                it.rarity
            )
        }

        //填充装备构造树
        rawEquipmentList?.forEach { raw ->
            if (raw.craft_flg == 1) {
                val craftMap = mutableMapOf<Item, Int>()
                equipmentMap[raw.equipment_id]?.let { equipment ->
                    listOf(
                        Pair(raw.condition_equipment_id_1, raw.consume_num_1),
                        Pair(raw.condition_equipment_id_2, raw.consume_num_2),
                        Pair(raw.condition_equipment_id_3, raw.consume_num_3),
                        Pair(raw.condition_equipment_id_4, raw.consume_num_4),
                        Pair(raw.condition_equipment_id_5, raw.consume_num_5),
                        Pair(raw.condition_equipment_id_6, raw.consume_num_6),
                        Pair(raw.condition_equipment_id_7, raw.consume_num_7),
                        Pair(raw.condition_equipment_id_8, raw.consume_num_8),
                        Pair(raw.condition_equipment_id_9, raw.consume_num_9),
                        Pair(raw.condition_equipment_id_10, raw.consume_num_10)
                    ).filter { (itemId, _) -> itemId != 0 }.forEach { (itemId, consumeCount) ->
                        when(itemId) {
                            in 101000..112999, in 10000000..10999999  -> {
                                equipmentMap[itemId]?.let {
                                    craftMap[it] = consumeCount
                                }
                            }
                            in 113000..139999, in 11000000..13999999 -> {
                                equipmentPieceMap[itemId]?.let { piece ->
                                    craftMap[piece] = consumeCount
                                }
                            }
                        }
                    }
                    equipment.craftMap = craftMap
                }
            }
        }
        //增加一个通用的未实装装备
        equipmentMap[999999] = Equipment.getNull
        return equipmentMap
    }
}