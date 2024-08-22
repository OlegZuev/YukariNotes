package com.github.olegzuev.yukarinotes.data.action;

import com.github.olegzuev.yukarinotes.R;
import com.github.olegzuev.yukarinotes.common.I18N;
import com.github.olegzuev.yukarinotes.data.Property;

public class ChangeEnergyByDamageAction extends ActionParameter {

    @Override
    protected void childInit() {
        super.childInit();
        actionValues.add(new ActionValue(actionValue1.value, actionValue2.value, eActionValue.VALUE1, eActionValue.VALUE2, null));
    }

    @Override
    public String localizedDetail(int level, Property property) {
        switch (actionDetail1) {
            case 1:
                return I18N.getString(R.string.Adds_s1_marks_to_s5_max_s2_id_d3_lasts_for_s4_sec_removes_1_mark_while_taking_dmg_and_restores_s6_tp,
                        actionValue3.valueString(),
                        actionValue4.valueString(),
                        actionDetail2,
                        actionValue5.valueString(),
                        targetParameter.buildTargetClause(),
                        buildExpression(level, actionValues, null, property)
                        );
        }
        return super.localizedDetail(level, property);
    }
}
