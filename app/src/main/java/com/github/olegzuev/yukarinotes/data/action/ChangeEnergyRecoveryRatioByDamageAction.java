package com.github.olegzuev.yukarinotes.data.action;

import com.github.olegzuev.yukarinotes.R;
import com.github.olegzuev.yukarinotes.common.I18N;
import com.github.olegzuev.yukarinotes.data.Property;
import com.github.olegzuev.yukarinotes.data.Skill;

public class ChangeEnergyRecoveryRatioByDamageAction extends ActionParameter {

    @Override
    protected void childInit() {
        super.childInit();
    }

    protected String getChildrenActionString() {
        StringBuilder childrenActionString = new StringBuilder();
        if (childrenAction != null) {
            for (Skill.Action action : childrenAction) {
                childrenActionString.append(action.getActionId() % 10).append(", ");
            }
            childrenActionString.delete(childrenActionString.lastIndexOf(", "), childrenActionString.length());
        }
        return childrenActionString.toString();
    }

    @Override
    public String localizedDetail(int level, Property property) {
        return I18N.getString(R.string.change_energy_recovery_ratio_of_action_s1_to_s2_when_s3_get_damage,
                getChildrenActionString(),
                actionValue1.valueString(),
                targetParameter.buildTargetClause());
    }
}
