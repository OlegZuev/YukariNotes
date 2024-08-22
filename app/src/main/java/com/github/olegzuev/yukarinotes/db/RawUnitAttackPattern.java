package com.github.olegzuev.yukarinotes.db;

import com.github.olegzuev.yukarinotes.data.AttackPattern;

import java.util.ArrayList;
import java.util.List;

public class RawUnitAttackPattern {
    public int pattern_id;
    public int unit_id;
    public int loop_start;
    public int loop_end;
    public int atk_pattern_1;
    public int atk_pattern_2;
    public int atk_pattern_3;
    public int atk_pattern_4;
    public int atk_pattern_5;
    public int atk_pattern_6;
    public int atk_pattern_7;
    public int atk_pattern_8;
    public int atk_pattern_9;
    public int atk_pattern_10;
    public int atk_pattern_11;
    public int atk_pattern_12;
    public int atk_pattern_13;
    public int atk_pattern_14;
    public int atk_pattern_15;
    public int atk_pattern_16;
    public int atk_pattern_17;
    public int atk_pattern_18;
    public int atk_pattern_19;
    public int atk_pattern_20;

    public AttackPattern getAttackPattern(){
        List<Integer> attackPatternList = new ArrayList<>();

        if (atk_pattern_1 != 0) { attackPatternList.add(atk_pattern_1); }
        if (atk_pattern_2 != 0) { attackPatternList.add(atk_pattern_2); }
        if (atk_pattern_3 != 0) { attackPatternList.add(atk_pattern_3); }
        if (atk_pattern_4 != 0) { attackPatternList.add(atk_pattern_4); }
        if (atk_pattern_5 != 0) { attackPatternList.add(atk_pattern_5); }
        if (atk_pattern_6 != 0) { attackPatternList.add(atk_pattern_6); }
        if (atk_pattern_7 != 0) { attackPatternList.add(atk_pattern_7); }
        if (atk_pattern_8 != 0) { attackPatternList.add(atk_pattern_8); }
        if (atk_pattern_9 != 0) { attackPatternList.add(atk_pattern_9); }
        if (atk_pattern_10 != 0) { attackPatternList.add(atk_pattern_10); }
        if (atk_pattern_11 != 0) { attackPatternList.add(atk_pattern_11); }
        if (atk_pattern_12 != 0) { attackPatternList.add(atk_pattern_12); }
        if (atk_pattern_13 != 0) { attackPatternList.add(atk_pattern_13); }
        // Skipping atk_pattern_14 on purpose: mistake? deliberately? only cy knows
        // if (atk_pattern_14 != 0) { attackPatternList.add(atk_pattern_14); }
        if (atk_pattern_15 != 0) { attackPatternList.add(atk_pattern_15); }
        if (atk_pattern_16 != 0) { attackPatternList.add(atk_pattern_16); }
        if (atk_pattern_17 != 0) { attackPatternList.add(atk_pattern_17); }
        if (atk_pattern_18 != 0) { attackPatternList.add(atk_pattern_18); }
        if (atk_pattern_19 != 0) { attackPatternList.add(atk_pattern_19); }
        if (atk_pattern_20 != 0) { attackPatternList.add(atk_pattern_20); }

        int real_end = 0;
        if (loop_end >= 14) {
            real_end = loop_end - 1;
        } else {
            real_end = loop_end;
        }
        return new AttackPattern(
                pattern_id,
                unit_id,
                loop_start,
                real_end,
                attackPatternList
        );
    }
}
