package com.github.olegzuev.yukarinotes.db;


import com.github.olegzuev.yukarinotes.data.ClanBattlePhase;

public class RawClanBattlePhase {
    public int clan_battle_id;
    public int phase;
    public int wave_group_id_1;
    public int wave_group_id_2;
    public int wave_group_id_3;
    public int wave_group_id_4;
    public int wave_group_id_5;

    public ClanBattlePhase getClanBattlePhase(){
        return new ClanBattlePhase(
                phase,
                wave_group_id_1,
                wave_group_id_2,
                wave_group_id_3,
                wave_group_id_4,
                wave_group_id_5
                );
    }
}
