package com.github.olegzuev.yukarinotes.db;

import java.util.HashMap;
import java.util.Map;

public class RawResistData {
    public int resist_status_id;
    public int ailment_1;
    public int ailment_2;
    public int ailment_3;
    public int ailment_4;
    public int ailment_5;
    public int ailment_6;
    public int ailment_7;
    public int ailment_8;
    public int ailment_9;
    public int ailment_10;
    public int ailment_11;
    public int ailment_12;
    public int ailment_13;
    public int ailment_14;
    public int ailment_15;
    public int ailment_16;
    public int ailment_17;
    public int ailment_18;
    public int ailment_19;
    public int ailment_20;
    public int ailment_21;
    public int ailment_22;
    public int ailment_23;
    public int ailment_24;
    public int ailment_25;
    public int ailment_26;
    public int ailment_27;
    public int ailment_28;
    public int ailment_29;
    public int ailment_30;
    public int ailment_31;
    public int ailment_32;
    public int ailment_33;
    public int ailment_34;
    public int ailment_35;
    public int ailment_36;
    public int ailment_37;
    public int ailment_38;
    public int ailment_39;
    public int ailment_40;
    public int ailment_41;
    public int ailment_42;
    public int ailment_43;
    public int ailment_44;
    public int ailment_45;
    public int ailment_46;
    public int ailment_47;
    public int ailment_48;
    public int ailment_49;
    public int ailment_50;
    public int ailment_51;
    public int ailment_52;

    public static Map<Integer, String> ailmentMap;

    public Map<String, Integer> getResistData(){
        if (ailmentMap == null) {
            ailmentMap = DBInfo.INSTANCE.getRawAilmentMap();
        }

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put(ailmentMap.get(1), ailment_1);
        resultMap.put(ailmentMap.get(2), ailment_2);
        resultMap.put(ailmentMap.get(3), ailment_3);
        resultMap.put(ailmentMap.get(4), ailment_4);
        resultMap.put(ailmentMap.get(5), ailment_5);
        resultMap.put(ailmentMap.get(6), ailment_6);
        resultMap.put(ailmentMap.get(7), ailment_7);
        resultMap.put(ailmentMap.get(8), ailment_8);
        resultMap.put(ailmentMap.get(9), ailment_9);
        resultMap.put(ailmentMap.get(10), ailment_10);
        resultMap.put(ailmentMap.get(11), ailment_11);
        resultMap.put(ailmentMap.get(12), ailment_12);
        resultMap.put(ailmentMap.get(13), ailment_13);
        resultMap.put(ailmentMap.get(14), ailment_14);
        resultMap.put(ailmentMap.get(15), ailment_15);
        resultMap.put(ailmentMap.get(16), ailment_16);
        resultMap.put(ailmentMap.get(17), ailment_17);
        resultMap.put(ailmentMap.get(18), ailment_18);
        resultMap.put(ailmentMap.get(19), ailment_19);
        resultMap.put(ailmentMap.get(20), ailment_20);
        resultMap.put(ailmentMap.get(21), ailment_21);
        resultMap.put(ailmentMap.get(22), ailment_22);
        resultMap.put(ailmentMap.get(23), ailment_23);
        resultMap.put(ailmentMap.get(24), ailment_24);
        resultMap.put(ailmentMap.get(25), ailment_25);
        resultMap.put(ailmentMap.get(26), ailment_26);
        resultMap.put(ailmentMap.get(27), ailment_27);
        resultMap.put(ailmentMap.get(28), ailment_28);
        resultMap.put(ailmentMap.get(29), ailment_29);
        resultMap.put(ailmentMap.get(30), ailment_30);
        resultMap.put(ailmentMap.get(31), ailment_31);
        resultMap.put(ailmentMap.get(32), ailment_32);
        resultMap.put(ailmentMap.get(33), ailment_33);
        resultMap.put(ailmentMap.get(34), ailment_34);
        resultMap.put(ailmentMap.get(35), ailment_35);
        resultMap.put(ailmentMap.get(36), ailment_36);
        resultMap.put(ailmentMap.get(37), ailment_37);
        resultMap.put(ailmentMap.get(38), ailment_38);
        resultMap.put(ailmentMap.get(39), ailment_39);
        resultMap.put(ailmentMap.get(40), ailment_40);
        resultMap.put(ailmentMap.get(41), ailment_41);
        resultMap.put(ailmentMap.get(42), ailment_42);
        resultMap.put(ailmentMap.get(43), ailment_43);
        resultMap.put(ailmentMap.get(44), ailment_44);
        resultMap.put(ailmentMap.get(45), ailment_45);
        resultMap.put(ailmentMap.get(46), ailment_46);
        resultMap.put(ailmentMap.get(47), ailment_47);
        resultMap.put(ailmentMap.get(48), ailment_48);
        resultMap.put(ailmentMap.get(49), ailment_49);
        resultMap.put(ailmentMap.get(50), ailment_50);
        resultMap.put(ailmentMap.get(51), ailment_51);
        resultMap.put(ailmentMap.get(52), ailment_52);

        return resultMap;
    }
}
