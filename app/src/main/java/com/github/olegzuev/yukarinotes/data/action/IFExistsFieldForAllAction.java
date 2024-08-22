package com.github.olegzuev.yukarinotes.data.action;

import com.github.olegzuev.yukarinotes.R;
import com.github.olegzuev.yukarinotes.common.I18N;
import com.github.olegzuev.yukarinotes.data.Property;

public class IFExistsFieldForAllAction extends ActionParameter {
    @Override
    protected void childInit() {
        super.childInit();
    }

    @Override
    public String localizedDetail(int level, Property property) {
        if(actionDetail2 !=0 && actionDetail3 != 0)
            return I18N.getString(R.string.Condition_if_the_specific_field_exists_then_use_d1_otherwise_d2,
                    getActionNum(actionDetail2), getActionNum(actionDetail3));
        else if(actionDetail2 != 0)
            return I18N.getString(R.string.Condition_if_the_specific_field_exists_then_use_d,
                    getActionNum(actionDetail2));
        else
            return super.localizedDetail(level, property);
    }
}
