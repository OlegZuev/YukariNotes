package com.github.olegzuev.yukarinotes.data.action;

import com.github.olegzuev.yukarinotes.R;
import com.github.olegzuev.yukarinotes.common.I18N;
import com.github.olegzuev.yukarinotes.data.Property;
import com.github.olegzuev.yukarinotes.data.PropertyKey;

public class HealAction extends ActionParameter {

    private ClassModifier healClass;
    private PercentModifier percentModifier;

    @Override
    protected void childInit() {
        healClass = ClassModifier.parse(actionDetail1);
        percentModifier = PercentModifier.parse((int)actionValue1.value);
        switch (healClass){
            case magical:
                actionValues.add(new ActionValue(actionValue4, actionValue5, PropertyKey.magicStr));
                actionValues.add(new ActionValue(actionValue2, actionValue3, null));
                break;
            case physical:
                actionValues.add(new ActionValue(actionValue4, actionValue5, PropertyKey.atk));
                actionValues.add(new ActionValue(actionValue2, actionValue3, null));
                break;
            default:
                return;
        }
    }

    @Override
    public String localizedDetail(int level, Property property) {
        return I18N.getString(R.string.Restore_s1_s2_s3_HP, targetParameter.buildTargetClause(), buildExpression(level, null, null, property, true, false, false), percentModifier.description());
    }
}
