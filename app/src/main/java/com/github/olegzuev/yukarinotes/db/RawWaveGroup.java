package com.github.olegzuev.yukarinotes.db;

import com.github.olegzuev.yukarinotes.data.Enemy;
import com.github.olegzuev.yukarinotes.data.EnemyRewardData;
import com.github.olegzuev.yukarinotes.data.WaveGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RawWaveGroup {
    public int id;
    public int wave_group_id;
    public int enemy_id_1;
    public int drop_gold_1;
    public int drop_reward_id_1;
    public int enemy_id_2;
    public int drop_gold_2;
    public int drop_reward_id_2;
    public int enemy_id_3;
    public int drop_gold_3;
    public int drop_reward_id_3;
    public int enemy_id_4;
    public int drop_gold_4;
    public int drop_reward_id_4;
    public int enemy_id_5;
    public int drop_gold_5;
    public int drop_reward_id_5;

    public WaveGroup getWaveGroup(boolean needEnemy){
        WaveGroup waveGroup = new WaveGroup(id, wave_group_id);
        if (needEnemy) {
            List<Enemy> enemyList = new ArrayList<>();
            for (int enemyId : Arrays.asList(enemy_id_1, enemy_id_2, enemy_id_3, enemy_id_4, enemy_id_5)) {
                if (enemyId != 0) {
                    enemyList.add(DBInfo.INSTANCE.getRawEnemy(enemyId).getEnemy());
                }
            }
            waveGroup.setEnemyList(enemyList);
        }

        List<EnemyRewardData> rewardDataList = new ArrayList<>();
        for (int dropRewardId : Arrays.asList(drop_reward_id_1, drop_reward_id_2, drop_reward_id_3, drop_reward_id_4, drop_reward_id_5)) {
            if (dropRewardId != 0) {
                rewardDataList.add(DBHelper.get().getEnemyRewardData(dropRewardId).getEnemyRewardData());
            }
        }

        waveGroup.setDropGoldList(new ArrayList<>(Arrays.asList(drop_gold_1, drop_gold_2, drop_gold_3, drop_gold_4, drop_gold_5)));
        waveGroup.setDropRewardList(rewardDataList);

        return waveGroup;
    }
}
