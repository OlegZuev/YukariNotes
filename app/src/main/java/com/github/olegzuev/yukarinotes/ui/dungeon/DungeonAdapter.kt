package com.github.olegzuev.yukarinotes.ui.dungeon

import android.view.View
import androidx.navigation.findNavController
import com.github.olegzuev.yukarinotes.R
import com.github.olegzuev.yukarinotes.data.Dungeon
import com.github.olegzuev.yukarinotes.databinding.ListItemDungeonBinding
import com.github.olegzuev.yukarinotes.ui.shared.SharedViewModelClanBattle
import com.github.olegzuev.yukarinotes.ui.base.BaseRecyclerAdapter

class DungeonAdapter(
    private val sharedClanBattle: SharedViewModelClanBattle
) : BaseRecyclerAdapter<Dungeon, ListItemDungeonBinding>(R.layout.list_item_dungeon) {

    override fun onBindViewHolder(holder: VH<ListItemDungeonBinding>, position: Int) {
        with(holder.binding){
            val thisDungeon = itemList[position]
            dungeon = thisDungeon
            textDungeonDescription.text = thisDungeon.description
            mode.text = thisDungeon.modeText
            clickListener = View.OnClickListener {
                sharedClanBattle.mSetSelectedBoss(thisDungeon.dungeonBoss)
                it.findNavController().navigate(
                    DungeonFragmentDirections.actionNavDungeonToNavEnemy()
                )
            }
            executePendingBindings()
        }
    }
}