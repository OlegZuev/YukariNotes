package com.github.olegzuev.yukarinotes.ui.setting.log

import android.view.View
import androidx.navigation.findNavController
import com.github.olegzuev.yukarinotes.R
import com.github.olegzuev.yukarinotes.databinding.ItemSelectableTextBinding
import com.github.olegzuev.yukarinotes.ui.base.BaseRecyclerAdapter
import java.io.File

class LogAdapter : BaseRecyclerAdapter<File, ItemSelectableTextBinding>(R.layout.item_selectable_text) {
    override fun onBindViewHolder(holder: VH<ItemSelectableTextBinding>, position: Int) {
        with(holder.binding) {
            val item = itemList[position]
            text = item.name
            clickListener = View.OnClickListener {
                if (item.length() < 1024 * 1024 * 1024) {
                    val action = LogFragmentDirections.actionNavLogToNavLogText().setLogText(item.readText())
                    it.findNavController().navigate(action)
                }
            }
        }
    }
}