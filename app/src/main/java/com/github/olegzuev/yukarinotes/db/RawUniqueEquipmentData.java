package com.github.olegzuev.yukarinotes.db;

import com.github.olegzuev.yukarinotes.data.Chara;
import com.github.olegzuev.yukarinotes.data.Equipment;
import com.github.olegzuev.yukarinotes.data.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RawUniqueEquipmentData {
    public int unit_id;
    public int equipment_id;
    public String equipment_name;
    public String description;
    public int promotion_level;
    public int craft_flg;
    public int equipment_enhance_point;
    public int sale_price;
    public int require_level;
    public double hp;
    public double atk;
    public double magic_str;
    public double def;
    public double magic_def;
    public double physical_critical;
    public double magic_critical;
    public double wave_hp_recovery;
    public double wave_energy_recovery;
    public double dodge;
    public double physical_penetrate;
    public double magic_penetrate;
    public double life_steal;
    public double hp_recovery_rate;
    public double energy_recovery_rate;
    public double energy_reduce_rate;
    public double accuracy;
    public int item_id_1;
    public int consume_num_1;
    public int item_id_2;
    public int consume_num_2;
    public int item_id_3;
    public int consume_num_3;
    public int item_id_4;
    public int consume_num_4;
    public int item_id_5;
    public int consume_num_5;
    public int item_id_6;
    public int consume_num_6;
    public int item_id_7;
    public int consume_num_7;
    public int item_id_8;
    public int consume_num_8;
    public int item_id_9;
    public int consume_num_9;
    public int item_id_10;
    public int consume_num_10;

    public Equipment getCharaUniqueEquipment(Chara chara){
        var enhanceData = DBInfo.INSTANCE.getRawUniqueEquipmentEnhanceData().get(equipment_id);
        ArrayList<Property> uniqueEquipEnhanceProperties = new ArrayList<>();
        for (RawUniqueEquipmentEnhanceData rawData: enhanceData) {
            uniqueEquipEnhanceProperties.add(rawData.getProperty());
        }
        return new Equipment(
                equipment_id,
                equipment_name,
                description.replace("\\n", ""),
                promotion_level,
                craft_flg,
                equipment_enhance_point,
                sale_price,
                require_level,
                equipment_id % 10 == 1 ? chara.getMaxUniqueEquipmentLevel() : 5,
                this.getProperty(),
                uniqueEquipEnhanceProperties,
                "",
                0
        );
    }

    public Property getProperty(){
        return new Property(
                hp,
                atk,
                magic_str,
                def,
                magic_def,
                physical_critical,
                magic_critical,
                wave_hp_recovery,
                wave_energy_recovery,
                dodge,
                physical_penetrate,
                magic_penetrate,
                life_steal,
                hp_recovery_rate,
                energy_recovery_rate,
                energy_reduce_rate,
                accuracy
        );
    }

    public List<Integer> getItemIdList() {
        return Arrays.asList(
                item_id_1,
                item_id_2,
                item_id_3,
                item_id_4,
                item_id_5,
                item_id_6,
                item_id_7,
                item_id_8,
                item_id_9,
                item_id_10
        );
    }

    public List<Integer> getConsumeIdList() {
        return Arrays.asList(
                consume_num_1,
                consume_num_2,
                consume_num_3,
                consume_num_4,
                consume_num_5,
                consume_num_6,
                consume_num_7,
                consume_num_8,
                consume_num_9,
                consume_num_10
        );
    }
}
