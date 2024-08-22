package com.github.olegzuev.yukarinotes.data.action;

import com.github.olegzuev.yukarinotes.R;
import com.github.olegzuev.yukarinotes.common.I18N;
import com.github.olegzuev.yukarinotes.data.Property;

public class CountDownAction extends ActionParameter {
    @Override
    protected void childInit() {
        super.childInit();
    }

    @Override
    public String localizedDetail(int level, Property property) {
        return I18N.getString(R.string.Set_a_countdown_timer_on_s1_trigger_effect_d2_after_s3_sec,
                targetParameter.buildTargetClause(), getActionNum(actionDetail1), actionValue1.valueString());
    }
}
