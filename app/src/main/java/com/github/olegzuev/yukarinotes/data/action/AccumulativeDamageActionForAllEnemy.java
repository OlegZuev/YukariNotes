package com.github.olegzuev.yukarinotes.data.action;

import com.github.olegzuev.yukarinotes.R;
import com.github.olegzuev.yukarinotes.common.I18N;
import com.github.olegzuev.yukarinotes.data.Property;

import java.math.RoundingMode;

public class AccumulativeDamageActionForAllEnemy extends AccumulativeDamageAction {
    @Override
    public String localizedDetail(int level, Property property) {
        return I18N.getString(R.string.Add_additional_s1_damage_per_attack_with_max_s2_stacks_to_current_target_for_all_enemy,
                buildExpression(level, property), buildExpression(level, stackValues, RoundingMode.FLOOR, property));
    }
}
