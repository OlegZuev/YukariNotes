package com.github.olegzuev.yukarinotes.data.action;

import com.github.olegzuev.yukarinotes.R;
import com.github.olegzuev.yukarinotes.common.I18N;
import com.github.olegzuev.yukarinotes.data.Property;

import java.math.RoundingMode;

public class ChangeParameterFieldAction extends AuraAction {

    @Override
    protected void childInit() {
        super.childInit();
        actionValues.clear();
        actionValues.add(new ActionValue(actionValue1, actionValue2, null));
        durationValues.clear();
        durationValues.add(new ActionValue(actionValue3, actionValue4, null));
        super.percentModifier = PercentModifier.parse(actionDetail2);
    }

    @Override
    public String localizedDetail(int level, Property property) {
        if(targetParameter.targetType == TargetType.absolute){
            return I18N.getString(R.string.Summon_a_field_of_radius_d1_to_s2_s3_s4_s5_for_s6_sec,
                    (int)actionValue5.value,
                    auraActionType.description(),
                    targetParameter.buildTargetClause(),
                    buildExpression(level, RoundingMode.UP, property),
                    auraType.description(),
                    buildExpression(level, durationValues, RoundingMode.UNNECESSARY, property),
                    percentModifier.description());
        } else {
            return I18N.getString(R.string.Summon_a_field_of_radius_d1_at_position_of_s2_to_s3_s4_s5_for_s6_sec,
                    (int)actionValue5.value,
                    targetParameter.buildTargetClause(),
                    auraActionType.description(),
                    buildExpression(level, RoundingMode.UP, property),
                    auraType.description(),
                    buildExpression(level, durationValues, RoundingMode.UNNECESSARY, property),
                    percentModifier.description());
        }
    }
}
