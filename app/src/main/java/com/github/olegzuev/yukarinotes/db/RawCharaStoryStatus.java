package com.github.olegzuev.yukarinotes.db;

import com.github.olegzuev.yukarinotes.data.OneStoryStatus;
import com.github.olegzuev.yukarinotes.data.Chara;
import com.github.olegzuev.yukarinotes.data.CharaStoryStatus;

import java.util.ArrayList;
import java.util.List;

public class RawCharaStoryStatus {
    public int story_id;
    public String unlock_story_name;
    public int status_type_1;
    public int status_rate_1;
    public int status_type_2;
    public int status_rate_2;
    public int status_type_3;
    public int status_rate_3;
    public int status_type_4;
    public int status_rate_4;
    public int status_type_5;
    public int status_rate_5;
    public int chara_id_1;
    public int chara_id_2;
    public int chara_id_3;
    public int chara_id_4;
    public int chara_id_5;
    public int chara_id_6;
    public int chara_id_7;
    public int chara_id_8;
    public int chara_id_9;
    public int chara_id_10;

    public OneStoryStatus getCharaStoryStatus(Chara chara){
        List<CharaStoryStatus> list = new ArrayList<>();
        if (status_type_1 != 0) list.add(new CharaStoryStatus(chara.getCharaId(), status_type_1, status_rate_1));
        if (status_type_2 != 0) list.add(new CharaStoryStatus(chara.getCharaId(), status_type_2, status_rate_2));
        if (status_type_3 != 0) list.add(new CharaStoryStatus(chara.getCharaId(), status_type_3, status_rate_3));
        if (status_type_4 != 0) list.add(new CharaStoryStatus(chara.getCharaId(), status_type_4, status_rate_4));
        if (status_type_5 != 0) list.add(new CharaStoryStatus(chara.getCharaId(), status_type_5, status_rate_5));
        return new OneStoryStatus(story_id, unlock_story_name, chara_id_1, list);
    }
}
