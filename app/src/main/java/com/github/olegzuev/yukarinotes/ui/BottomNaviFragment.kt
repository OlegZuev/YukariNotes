package com.github.olegzuev.yukarinotes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.olegzuev.yukarinotes.R
import com.github.olegzuev.yukarinotes.databinding.FragmentBottomNavigationBinding
import com.github.olegzuev.yukarinotes.user.UserSettings

class BottomNaviFragment : Fragment() {

    private lateinit var adapter: BottomNaviAdapter
    private var currentIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = BottomNaviAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentBottomNavigationBinding.inflate(inflater, container, false)
        binding.bottomNavView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_menu_chara -> switchFragments(CHARA_INDEX)
                R.id.bottom_menu_drop -> switchFragments(DROP_INDEX)
                R.id.bottom_menu_clan_battle -> switchFragments(CLAN_BATTLE_INDEX)
                R.id.bottom_menu_menu -> {
                    switchFragments(MENU_INDEX)
                    if (UserSettings.get().getBadgeVisibility()) {
                        UserSettings.get().setBadgeVisibility(false)
                        binding.bottomNavView.removeBadge(R.id.bottom_menu_menu)
                    }
                }
            }
            true
        }
        if (currentIndex == -1) {
            switchFragments(CHARA_INDEX)
        }
        if (UserSettings.get().getBadgeVisibility()) {
            binding.bottomNavView.getOrCreateBadge(R.id.bottom_menu_menu)
        }
        return binding.root
    }

    private fun switchFragments(index: Int) {
        currentIndex = index
        val tag = index.toString()
        val transaction = childFragmentManager.beginTransaction()

        childFragmentManager.findFragmentById(R.id.frame_container)?.let {
            transaction.detach(it)
        }

        var fragment = childFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = adapter.createFragment(index)
            transaction.add(R.id.frame_container, fragment, tag)
        } else {
            transaction.attach(fragment)
        }

        transaction
            .setPrimaryNavigationFragment(fragment)
            .setReorderingAllowed(true)
            .commit()
    }
}