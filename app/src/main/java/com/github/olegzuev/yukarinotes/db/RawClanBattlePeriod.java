package com.github.olegzuev.yukarinotes.db;

import com.github.olegzuev.yukarinotes.data.ClanBattlePeriod;
import com.github.olegzuev.yukarinotes.utils.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RawClanBattlePeriod {
    public int clan_battle_id;
    public String start_time;
    public String end_time;

    public ClanBattlePeriod transToClanBattlePeriod(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d H:m:s");
        return new ClanBattlePeriod(
                clan_battle_id,
                Utils.CustomLocalDataTimeParse(start_time, formatter),
                Utils.CustomLocalDataTimeParse(end_time, formatter)
        );
    }
}
