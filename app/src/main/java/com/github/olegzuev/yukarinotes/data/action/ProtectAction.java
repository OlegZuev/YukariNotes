package com.github.olegzuev.yukarinotes.data.action;

import com.github.olegzuev.yukarinotes.R;
import com.github.olegzuev.yukarinotes.common.I18N;
import com.github.olegzuev.yukarinotes.data.Property;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ProtectAction extends ActionParameter {

    protected List<ActionValue> counterValues = new ArrayList<>();
    protected List<ActionValue> durationValues = new ArrayList<>();

    @Override
    protected void childInit() {
        super.childInit();
        counterValues.add(new ActionValue(actionValue1, actionValue2, null));
        durationValues.add(new ActionValue(actionValue3, actionValue4, null));
    }

    @Override
    public String localizedDetail(int level, Property property) {
        return I18N.getString(R.string.Protect_s1_from_certain_skill_max_s2_for_s3_sec,
            targetParameter.buildTargetClause(),
            buildExpression(level, counterValues, RoundingMode.FLOOR, property),
            buildExpression(level, durationValues, RoundingMode.UNNECESSARY, property)
        );
    }
}
