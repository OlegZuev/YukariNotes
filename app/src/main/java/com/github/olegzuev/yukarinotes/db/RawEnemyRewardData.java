package com.github.olegzuev.yukarinotes.db;

import com.github.olegzuev.yukarinotes.data.EnemyRewardData;
import com.github.olegzuev.yukarinotes.data.RewardData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kotlin.Triple;
import kotlin.Pair;

public class RawEnemyRewardData {
    public int drop_reward_id;
    public int drop_count;
    public int reward_type_1;
    public int reward_id_1;
    public int reward_num_1;
    public int odds_1;
    public int reward_type_2;
    public int reward_id_2;
    public int reward_num_2;
    public int odds_2;
    public int reward_type_3;
    public int reward_id_3;
    public int reward_num_3;
    public int odds_3;
    public int reward_type_4;
    public int reward_id_4;
    public int reward_num_4;
    public int odds_4;
    public int reward_type_5;
    public int reward_id_5;
    public int reward_num_5;
    public int odds_5;

    public EnemyRewardData getEnemyRewardData() {
        List<RewardData> rewardDataList = new ArrayList<>();
        for (var rewardData : Arrays.asList(
                new Pair<>(reward_id_1, new Triple<>(reward_type_1, reward_num_1, odds_1)),
                new Pair<>(reward_id_2, new Triple<>(reward_type_2, reward_num_2, odds_2)),
                new Pair<>(reward_id_3, new Triple<>(reward_type_3, reward_num_3, odds_3)),
                new Pair<>(reward_id_4, new Triple<>(reward_type_4, reward_num_4, odds_4)),
                new Pair<>(reward_id_5, new Triple<>(reward_type_5, reward_num_5, odds_5)))) {
            if (rewardData.component1() != 0) {
                rewardDataList.add(new RewardData(
                        rewardData.component2().component1(),
                        rewardData.component1(),
                        rewardData.component2().component2(),
                        rewardData.component2().component3()
                ));
            }
        }
        return new EnemyRewardData(rewardDataList);
    }

}
