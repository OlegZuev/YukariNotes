package com.github.olegzuev.yukarinotes.db;

import java.util.Arrays;
import java.util.List;

public class RawUnitPromotion {
    public int unit_id;
    public int promotion_level;
    public int equip_slot_1;
    public int equip_slot_2;
    public int equip_slot_3;
    public int equip_slot_4;
    public int equip_slot_5;
    public int equip_slot_6;

    public List<Integer> getCharaSlots(){
        return Arrays.asList(
                equip_slot_1,
                equip_slot_2,
                equip_slot_3,
                equip_slot_4,
                equip_slot_5,
                equip_slot_6
        );
    }
}
