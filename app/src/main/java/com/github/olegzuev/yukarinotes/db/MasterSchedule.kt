package com.github.olegzuev.yukarinotes.db

import com.github.olegzuev.yukarinotes.data.CampaignSchedule
import com.github.olegzuev.yukarinotes.data.CampaignType
import com.github.olegzuev.yukarinotes.data.EventSchedule
import com.github.olegzuev.yukarinotes.data.EventType
import com.github.olegzuev.yukarinotes.utils.Utils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MasterSchedule {
    fun getSchedule(nowTime: LocalDateTime?): MutableList<EventSchedule> {
        val scheduleList = mutableListOf<EventSchedule>()
        val formatter = DateTimeFormatter.ofPattern("yyyy/M/d H:mm:ss")

        DBHelper.get().getCampaignSchedule(null)?.forEach {
            val campaignType = CampaignType.parse(it.campaign_category)
            scheduleList.add(
                CampaignSchedule(
                    it.id, "", EventType.Campaign,
                    Utils.CustomLocalDataTimeParse(it.start_time, formatter),
                    Utils.CustomLocalDataTimeParse(it.end_time, formatter),
                    it.campaign_category, campaignType, it.value, it.system_id
                )
            )
        }
        DBHelper.get().getHatsuneSchedule(null)?.forEach {
            scheduleList.add(
                EventSchedule(
                    it.event_id, it.title, EventType.Hatsune,
                    Utils.CustomLocalDataTimeParse(it.start_time, formatter),
                    Utils.CustomLocalDataTimeParse(it.end_time, formatter)
                )
            )
        }
        DBHelper.get().getTowerSchedule(null)?.forEach {
            scheduleList.add(
                EventSchedule(
                    it.tower_schedule_id, "",
                    EventType.Tower,
                    Utils.CustomLocalDataTimeParse(it.start_time, formatter),
                    Utils.CustomLocalDataTimeParse(it.end_time, formatter)
                )
            )
        }

        if (nowTime == null) {
            DBHelper.get().getFreeGachaSchedule(null)?.forEach {
                scheduleList.add(
                    EventSchedule(
                        it.campaign_id, "", EventType.Gacha,
                        Utils.CustomLocalDataTimeParse(it.start_time, formatter),
                        Utils.CustomLocalDataTimeParse(it.end_time, formatter)
                    )
                )
            }
            DBHelper.get().getClanBattlePeriod()?.forEach {
                scheduleList.add(
                    EventSchedule(
                        it.clan_battle_id,
                        "",
                        EventType.ClanBattle,
                        Utils.CustomLocalDataTimeParse(it.start_time, formatter),
                        Utils.CustomLocalDataTimeParse(it.end_time, formatter)
                    )
                )
            }
        } else {
            //你说cy这么大个会社为什么连日期格式都不用标准的还得从程序上判断
            val iterator = scheduleList.iterator()
            while (iterator.hasNext()) {
                if (iterator.next().endTime.isBefore(nowTime)) {
                    iterator.remove()
                }
            }
        }
        return scheduleList
    }
}