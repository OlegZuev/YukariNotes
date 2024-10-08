package com.github.olegzuev.yukarinotes.ui.charadetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.github.olegzuev.yukarinotes.R
import com.github.olegzuev.yukarinotes.common.I18N
import com.github.olegzuev.yukarinotes.data.Chara
import com.github.olegzuev.yukarinotes.databinding.FragmentCharaDetailsBinding
import com.github.olegzuev.yukarinotes.ui.shared.SharedViewModelChara
import com.github.olegzuev.yukarinotes.ui.shared.SharedViewModelCharaFactory
import com.github.olegzuev.yukarinotes.ui.base.AttackPatternContainerAdapter
import com.github.olegzuev.yukarinotes.ui.base.BaseHintAdapter
import com.github.olegzuev.yukarinotes.user.UserSettings
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// TODO: 改成使用ViewType接口和适配器，避免NestedScrollView一次性渲染全部视图造成丢帧
class CharaDetailsFragment : Fragment(), View.OnClickListener {

    private lateinit var detailsViewModel: CharaDetailsViewModel
    private lateinit var sharedChara: SharedViewModelChara
    private lateinit var binding: FragmentCharaDetailsBinding
    private val args: CharaDetailsFragmentArgs by navArgs()

    private val adapterSkill by lazy { SkillAdapter(sharedChara) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedChara = ViewModelProvider(requireActivity()).get(SharedViewModelChara::class.java)
        detailsViewModel = ViewModelProvider(this, SharedViewModelCharaFactory(sharedChara))[CharaDetailsViewModel::class.java]

        sharedElementEnterTransition =
            TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.move)!!.setDuration(300)

        sharedElementReturnTransition =
            TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.move)!!.setDuration(300)
    }

//    override fun onResume() {
//        super.onResume()
//        binding.toolbar.menu.findItem(R.id.menu_chara_show_expression).isChecked = UserSettings.get().getExpression()
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCharaDetailsBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            detailsItemChara.transitionName = "transItem_${args.charaId}"
            if (sharedChara.backFlag)
                appbar.setExpanded(false, false)
            detailsVM = detailsViewModel
            clickListener = this@CharaDetailsFragment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_chara_customize -> {
                        Navigation.findNavController(binding.root).navigate(
                            CharaDetailsFragmentDirections.actionNavCharaDetailsToNavAnalyze()
                        )
                    }
                    R.id.menu_chara_show_expression -> {
                        val singleItems = I18N.getStringArray(R.array.setting_kill_expression_options)
                        val checkedItem = UserSettings.get().getExpression()
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(I18N.getString(R.string.setting_kill_expression_title))
                            .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                                if (UserSettings.get().getExpression() != which) {
                                    UserSettings.get().setExpression(which)
                                    sharedChara.mSetSelectedChara(sharedChara.selectedChara)
                                    adapterSkill.notifyDataSetChanged()
                                }
                                dialog.dismiss()
                            }.show()
                    }
                }
                true
            }
        }

        // 技能循环
        val adapterAttackPattern = AttackPatternContainerAdapter(context).apply {
            initializeItems(detailsViewModel.mutableChara.value?.attackPatternList)
        }
        binding.attackPatternRecycler.apply {
            layoutManager = GridLayoutManager(context, 6).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when(adapterAttackPattern.getItemViewType(position)) {
                            BaseHintAdapter.HINT_TEXT -> 6
                            else -> 1
                        }
                    }
                }
            }
            adapter = adapterAttackPattern
        }

        // 技能 Recycler
        val layoutManagerSkill = LinearLayoutManager(context)
        binding.skillRecycler.apply {
            layoutManager = layoutManagerSkill
            adapter = adapterSkill
        }

        // 观察chara变化（1.0.0去掉rank下拉框后已经可以删掉了，留着备用）
        detailsViewModel.mutableChara.observe(viewLifecycleOwner, { chara: Chara ->
                binding.detailsVM = detailsViewModel
                adapterSkill.update(chara.skills)
        })
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.detailsItemCharaContainer) {
            val action =
                CharaDetailsFragmentDirections.actionNavCharaDetailsToNavCharaProfile()
            Navigation.findNavController(v).navigate(action)
        }
    }
}