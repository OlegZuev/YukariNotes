package com.github.olegzuev.yukarinotes.ui.charalist

import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.olegzuev.yukarinotes.R
import com.github.olegzuev.yukarinotes.common.I18N
import com.github.olegzuev.yukarinotes.data.Chara
import com.github.olegzuev.yukarinotes.ui.base.CharaListVT
import com.github.olegzuev.yukarinotes.ui.base.OnItemActionListener
import com.github.olegzuev.yukarinotes.ui.base.ViewType
import com.github.olegzuev.yukarinotes.ui.shared.SharedViewModelChara
import java.util.*

class CharaListViewModel(
    private val sharedViewModelChara: SharedViewModelChara
) : ViewModel() {

    fun getViewList(charaList: List<Chara>? = null): MutableList<ViewType<*>> {
        return if (charaList.isNullOrEmpty()) {
            mutableListOf<ViewType<*>>().apply {
                liveCharaList.value?.forEach {
                    add(CharaListVT(it))
                }
            }
        } else {
            mutableListOf<ViewType<*>>().apply {
                charaList.forEach {
                    add(CharaListVT(it))
                }
            }
        }
    }

    val liveCharaList = MutableLiveData<List<Chara>>()

    var selectedAttackType: String = "0"
    var selectedFilter: Filter = Filter.ANY
    var selectedSort: String = "0"
    var isAsc: Boolean = false
    var searchText: CharSequence = ""

    private val attackTypeMap = mapOf(
        0 to I18N.getString(R.string.ui_chip_any),
        1 to I18N.getString(R.string.ui_chip_atk_type_physical),
        2 to I18N.getString(R.string.ui_chip_atk_type_magical)
    )

    private val sortMap = mapOf(
        0 to I18N.getString(R.string.ui_chip_sort_new),
        1 to I18N.getString(R.string.ui_chip_sort_position),
        2 to I18N.getString(R.string.ui_chip_sort_physical_atk),
        3 to I18N.getString(R.string.ui_chip_sort_magical_atk),
        4 to I18N.getString(R.string.ui_chip_sort_physical_critical),
        5 to I18N.getString(R.string.ui_chip_sort_magical_critical),
        6 to I18N.getString(R.string.ui_chip_sort_physical_def),
        7 to I18N.getString(R.string.ui_chip_sort_magical_def),
        8 to I18N.getString(R.string.ui_chip_sort_hp),
        9 to I18N.getString(R.string.ui_chip_sort_effective_physical_hp),
        10 to I18N.getString(R.string.ui_chip_sort_effective_magical_hp),
        11 to I18N.getString(R.string.ui_chip_sort_tp_up),
        12 to I18N.getString(R.string.ui_chip_sort_tp_reduce),
        13 to I18N.getString(R.string.ui_chip_sort_name),
        14 to I18N.getString(R.string.ui_chip_sort_age),
        15 to I18N.getString(R.string.ui_chip_sort_height),
        16 to I18N.getString(R.string.ui_chip_sort_weight)
    )

    val dropDownValuesMap = mapOf<Int, Array<String>>(
        1 to attackTypeMap.values.toTypedArray(),
        2 to Filter.getAllLabels(),
        3 to sortMap.values.toTypedArray()
    )

    private fun filter(
        attackType: String?,
        filterID: Int?,
        sortValue: String?,
        asc: Boolean?,
        searchText: CharSequence?
    ) {
        selectedAttackType = attackType?: selectedAttackType
        selectedFilter = Filter.getByID(filterID) ?: selectedFilter
        sortValue?.apply {
            isAsc = if (this == selectedSort) !isAsc else false
            selectedSort = this
        }

        asc?.let { isAsc = it }

        val charaToShow: MutableList<Chara> = ArrayList()
        sharedViewModelChara.charaList.value?.forEach { chara ->
            if (searchText?.isNotEmpty() == true) {
                if (chara.unitName.lowercase().startsWith(searchText.toString().lowercase())
                    || chara.kana.lowercase().startsWith(searchText.toString().lowercase())
                ) {
                    setSortValue(chara, selectedSort)
                    charaToShow.add(chara)
                }
            } else if (checkAttackType(chara, selectedAttackType) && selectedFilter.checkChara(chara)) {
                setSortValue(chara, selectedSort)
                charaToShow.add(chara)
            }
        }

        charaToShow.sortWith(kotlin.Comparator { a: Chara, b: Chara ->
            val valueA : Int
            val valueB : Int
            when (selectedSort) {
                "0" -> {
                    return@Comparator if (b.startTime.isEqual(a.startTime)) 0 else if (b.startTime.isAfter(a.startTime) == isAsc) -1 else 1
                }
                "1" -> {
                    valueA = a.searchAreaWidth
                    valueB = b.searchAreaWidth
                }
                "2" -> {
                    valueA = a.charaProperty.getAtk()
                    valueB = b.charaProperty.getAtk()
                }
                "3" -> {
                    valueA = a.charaProperty.getMagicStr()
                    valueB = b.charaProperty.getMagicStr()
                }
                "4" -> {
                    valueA = a.charaProperty.getPhysicalCritical()
                    valueB = b.charaProperty.getPhysicalCritical()
                }
                "5" -> {
                    valueA = a.charaProperty.getMagicCritical()
                    valueB = b.charaProperty.getMagicCritical()
                }
                "6" -> {
                    valueA = a.charaProperty.getDef()
                    valueB = b.charaProperty.getDef()
                }
                "7" -> {
                    valueA = a.charaProperty.getMagicDef()
                    valueB = b.charaProperty.getMagicDef()
                }
                "8" -> {
                    valueA = a.charaProperty.getHp()
                    valueB = b.charaProperty.getHp()
                }
                "9" -> {
                    valueA = a.charaProperty.effectivePhysicalHP
                    valueB = b.charaProperty.effectivePhysicalHP
                }
                "10" -> {
                    valueA = a.charaProperty.effectiveMagicalHP
                    valueB = b.charaProperty.effectiveMagicalHP
                }
                "11" -> {
                    valueA = a.charaProperty.getEnergyRecoveryRate()
                    valueB = b.charaProperty.getEnergyRecoveryRate()
                }
                "12" -> {
                    valueA = a.charaProperty.getEnergyReduceRate()
                    valueB = b.charaProperty.getEnergyReduceRate()
                }
                "13" -> {
                    val nameA: String = a.unitName
                    val nameB: String = b.unitName
                    return@Comparator (if (isAsc) 1 else -1) * nameB.compareTo(nameA)
                }
                "14" -> {
                    valueA = try {
                        a.age.toInt()
                    } catch (ex: Exception) {
                        9999
                    }
                    valueB = try {
                        b.age.toInt()
                    } catch (ex: Exception) {
                        9999
                    }
                }
                "15" -> {
                    valueA = try {
                        a.height.toInt()
                    } catch (ex: Exception) {
                        9999
                    }
                    valueB = try {
                        b.height.toInt()
                    } catch (ex: Exception) {
                        9999
                    }
                }
                "16" -> {
                    valueA = try {
                        a.weight.toInt()
                    } catch (ex: Exception) {
                        9999
                    }
                    valueB = try {
                        b.weight.toInt()
                    } catch (ex: Exception) {
                        9999
                    }
                }
                else -> {
                    valueA = a.unitId
                    valueB = b.unitId
                }
            }
            (if (isAsc) -1 else 1) * valueB.compareTo(valueA)
        })
        liveCharaList.postValue(charaToShow)
    }

    fun filterDefault() {
        filter(selectedAttackType, selectedFilter.ordinal, selectedSort, isAsc, searchText)
    }

    enum class Filter {
        ANY {
            override fun checkChara(chara: Chara) = true
            override fun getLabel() = I18N.getString(R.string.ui_chip_any)
        },
        FRONT {
            override fun checkChara(chara: Chara) = chara.position == "1"
            override fun getLabel() = I18N.getString(R.string.ui_chip_position_forward)
        },
        MIDDLE {
            override fun checkChara(chara: Chara) = chara.position == "2"
            override fun getLabel() = I18N.getString(R.string.ui_chip_position_middle)
        },
        BACK {
            override fun checkChara(chara: Chara) = chara.position == "3"
            override fun getLabel() = I18N.getString(R.string.ui_chip_position_rear)
        },
        FIRE {
            override fun checkChara(chara: Chara) = chara.element == 1
            override fun getLabel() = I18N.getString(R.string.ui_chip_element_fire)
        },
        WATER {
            override fun checkChara(chara: Chara) = chara.element == 2
            override fun getLabel() = I18N.getString(R.string.ui_chip_element_water)
        },
        WIND {
            override fun checkChara(chara: Chara) = chara.element == 3
            override fun getLabel() = I18N.getString(R.string.ui_chip_element_wind)
        },
        LIGHT {
            override fun checkChara(chara: Chara) = chara.element == 4
            override fun getLabel() = I18N.getString(R.string.ui_chip_element_light)
        },
        DARK {
            override fun checkChara(chara: Chara) = chara.element == 5
            override fun getLabel() = I18N.getString(R.string.ui_chip_element_dark)
        };

        abstract fun checkChara(chara: Chara): Boolean
        abstract fun getLabel(): String

        companion object {
            fun getAllLabels(): Array<String> {
                return entries.map { it.getLabel() }.toTypedArray();
            }
            fun getByID(id: Int?): Filter? = entries.firstOrNull {it.ordinal == id}
        }
    }

    private fun checkAttackType(chara: Chara, type: String): Boolean {
        return type == "0" || type.toInt() == chara.atkType
    }

    private fun setSortValue(chara: Chara, sortValue: String) {
        when (sortValue) {
            "1" -> chara.sortValue = chara.searchAreaWidth.toString()
            "2" -> chara.sortValue = chara.charaProperty.getAtk().toString()
            "3" -> chara.sortValue = chara.charaProperty.getMagicStr().toString()
            "4" -> chara.sortValue = chara.charaProperty.getPhysicalCritical().toString()
            "5" -> chara.sortValue = chara.charaProperty.getMagicCritical().toString()
            "6" -> chara.sortValue = chara.charaProperty.getDef().toString()
            "7" -> chara.sortValue = chara.charaProperty.getMagicDef().toString()
            "8" -> chara.sortValue = chara.charaProperty.getHp().toString()
            "9" -> chara.sortValue = chara.charaProperty.effectivePhysicalHP.toString()
            "10" -> chara.sortValue = chara.charaProperty.effectiveMagicalHP.toString()
            "11" -> chara.sortValue = chara.charaProperty.getEnergyRecoveryRate().toString()
            "12" -> chara.sortValue = chara.charaProperty.getEnergyReduceRate().toString()
            "13" -> chara.sortValue = "" // No need to show value again
            "14" -> {
                if (chara.actualName == "出雲 宮子" || chara.actualName == "出云宫子") {
                    chara.sortValue = I18N.getString(R.string.aged_s, chara.age)
                } else {
                    chara.sortValue = chara.age
                }
            }
            "15" -> chara.sortValue = chara.height
            "16" -> chara.sortValue = chara.weight
            else -> chara.sortValue = ""
        }
    }

    val textWatcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable) {
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            searchText = s
            filterDefault()
        }
    }

    fun getSpinnerClickListener(type: Int): AdapterView.OnItemClickListener {
        return when (type) {
            1 -> AdapterView.OnItemClickListener { _, _, position, _ ->
                filter(position.toString(), null, null, null, searchText)
            }
            2 -> AdapterView.OnItemClickListener { _, _, position, _ ->
                filter(null, position, null, null, searchText)
            }
            3 -> AdapterView.OnItemClickListener { _, _, position, _ ->
                filter(null, null, position.toString(), null, searchText)
            }
            else -> throw IllegalStateException("Illegal spinner adapter type $type.")
        }
    }
}

interface OnCharaActionListener : OnItemActionListener {
    fun onCharaClickedListener(chara: Chara, position: Int)
}
