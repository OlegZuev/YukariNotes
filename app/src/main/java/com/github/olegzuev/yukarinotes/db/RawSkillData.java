package com.github.olegzuev.yukarinotes.db;

import com.github.olegzuev.yukarinotes.data.Skill;

public class RawSkillData {
    public int skill_id;
    public String name;
    public int skill_type;
    public int skill_area_width;
    public double skill_cast_time;
    public double boss_ub_cool_time;
    public int action_1;
    public int action_2;
    public int action_3;
    public int action_4;
    public int action_5;
    public int action_6;
    public int action_7;
    public int action_8;
    public int action_9;
    public int action_10;
    public int action_11;
    public int action_12;
    public int action_13;
    public int action_14;
    public int action_15;
    public int action_16;
    public int action_17;
    public int action_18;
    public int action_19;
    public int action_20;
    public int depend_action_1;
    public int depend_action_2;
    public int depend_action_3;
    public int depend_action_4;
    public int depend_action_5;
    public int depend_action_6;
    public int depend_action_7;
    public int depend_action_8;
    public int depend_action_9;
    public int depend_action_10;
    public int depend_action_11;
    public int depend_action_12;
    public int depend_action_13;
    public int depend_action_14;
    public int depend_action_15;
    public int depend_action_16;
    public int depend_action_17;
    public int depend_action_18;
    public int depend_action_19;
    public int depend_action_20;
    public String description;
    public int icon_type;

    public void setSkillData(Skill skill){
        skill.setSkillData(
                name,
                skill_type,
                skill_area_width,
                skill_cast_time,
                boss_ub_cool_time,
                description,
                icon_type
        );
        if (action_1 != 0) skill.getActions().add(skill.new Action(action_1, depend_action_1));
        if (action_2 != 0) skill.getActions().add(skill.new Action(action_2, depend_action_2));
        if (action_3 != 0) skill.getActions().add(skill.new Action(action_3, depend_action_3));
        if (action_4 != 0) skill.getActions().add(skill.new Action(action_4, depend_action_4));
        if (action_5 != 0) skill.getActions().add(skill.new Action(action_5, depend_action_5));
        if (action_6 != 0) skill.getActions().add(skill.new Action(action_6, depend_action_6));
        if (action_7 != 0) skill.getActions().add(skill.new Action(action_7, depend_action_7));
        if (action_8 != 0) skill.getActions().add(skill.new Action(action_8, depend_action_8));
        if (action_9 != 0) skill.getActions().add(skill.new Action(action_9, depend_action_9));
        if (action_10 != 0) skill.getActions().add(skill.new Action(action_10, depend_action_10));
        if (action_11 != 0) skill.getActions().add(skill.new Action(action_11, depend_action_11));
        if (action_12 != 0) skill.getActions().add(skill.new Action(action_12, depend_action_12));
        if (action_13 != 0) skill.getActions().add(skill.new Action(action_13, depend_action_13));
        if (action_14 != 0) skill.getActions().add(skill.new Action(action_14, depend_action_14));
        if (action_15 != 0) skill.getActions().add(skill.new Action(action_15, depend_action_15));
        if (action_16 != 0) skill.getActions().add(skill.new Action(action_16, depend_action_16));
        if (action_17 != 0) skill.getActions().add(skill.new Action(action_17, depend_action_17));
        if (action_18 != 0) skill.getActions().add(skill.new Action(action_18, depend_action_18));
        if (action_19 != 0) skill.getActions().add(skill.new Action(action_19, depend_action_19));
        if (action_20 != 0) skill.getActions().add(skill.new Action(action_20, depend_action_20));
    }
}
