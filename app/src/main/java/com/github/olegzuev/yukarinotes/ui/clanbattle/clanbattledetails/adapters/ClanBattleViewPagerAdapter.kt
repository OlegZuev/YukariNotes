package com.github.olegzuev.yukarinotes.ui.clanbattle.clanbattledetails.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.olegzuev.yukarinotes.data.ClanBattlePhase
import com.github.olegzuev.yukarinotes.ui.clanbattle.clanbattledetails.ClanBattleDetailsFragment

class ClanBattleViewPagerAdapter(
    fragment: Fragment,
    phaseList: MutableList<ClanBattlePhase>
) : FragmentStateAdapter(fragment) {

    private val tabFragmentCreator = mutableMapOf<Int, () -> ClanBattleDetailsFragment>()

    init {
        phaseList.forEach {
            tabFragmentCreator[it.phase - 1] = {
                ClanBattleDetailsFragment(
                    it
                )
            }
        }
    }

    override fun getItemCount() = tabFragmentCreator.size

    override fun createFragment(position: Int): Fragment {
        val index = tabFragmentCreator.size - position - 1
        return tabFragmentCreator[index]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}