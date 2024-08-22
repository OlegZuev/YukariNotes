package com.github.olegzuev.yukarinotes.ui.calendar

import com.github.olegzuev.yukarinotes.R
import com.github.olegzuev.yukarinotes.data.CampaignSchedule
import com.github.olegzuev.yukarinotes.data.EventSchedule
import com.github.olegzuev.yukarinotes.databinding.ItemScheduleBinding
import com.github.olegzuev.yukarinotes.ui.base.BaseRecyclerAdapter

class DayScheduleAdapter : BaseRecyclerAdapter<EventSchedule, ItemScheduleBinding>(R.layout.item_schedule) {
    override fun onBindViewHolder(holder: VH<ItemScheduleBinding>, position: Int) {
        with(holder.binding) {
            val item = itemList[position]
            schedule = item
            if (item is CampaignSchedule) {
                typeDot.setColorFilter(item.campaignType.shortColor())
            } else {
                typeDot.setColorFilter(item.type.color)
            }
            executePendingBindings()
        }
    }
}