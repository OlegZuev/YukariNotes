package com.github.olegzuev.yukarinotes.data.action;

import com.github.olegzuev.yukarinotes.R;
import com.github.olegzuev.yukarinotes.common.I18N;
import com.github.olegzuev.yukarinotes.data.Property;
import com.github.olegzuev.yukarinotes.data.PropertyKey;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class EnchantLifeStealAction extends ActionParameter {

    private List<ActionValue> stackValues = new ArrayList<>();

    @Override
    protected void childInit() {
        actionValues.add(new ActionValue(actionValue1, actionValue2, null));
        stackValues.add(new ActionValue(actionValue3, actionValue4, null));
    }

    @Override
    public String localizedDetail(int level, Property property) {
        return I18N.getString(R.string.Add_additional_s1_s2_to_s3_for_next_s4_attacks,
                buildExpression(level, property), PropertyKey.lifeSteal.description(), targetParameter.buildTargetClause(), buildExpression(level, stackValues, RoundingMode.FLOOR, property));
    }
}
