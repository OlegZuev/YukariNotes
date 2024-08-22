package com.github.olegzuev.yukarinotes.data.action;

import com.github.olegzuev.yukarinotes.R;
import com.github.olegzuev.yukarinotes.common.I18N;
import com.github.olegzuev.yukarinotes.data.Property;

public class IfContainsUnitGroupAction extends ActionParameter {
    @Override
    protected void childInit() {
        super.childInit();
    }

    @Override
    public String localizedDetail(int level, Property property) {
        return I18N.getString(R.string.Enabled_if_s1_in_group_d2_or_d3,
            targetParameter.buildTargetClause(true),
            actionDetail1,
            actionDetail2
        );
    }
}
