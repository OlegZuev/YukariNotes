package com.github.olegzuev.yukarinotes.ui.calendar.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.github.olegzuev.yukarinotes.R
import com.github.olegzuev.yukarinotes.databinding.FragmentEventNotificationBinding

class EventNotificationContainerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEventNotificationBinding.inflate(
            inflater, container, false
        ).apply {
            eventNotificationToolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }
        }

        childFragmentManager.beginTransaction()
            .replace(R.id.notification_container, EventNotificationSetting())
            .commit()

        return binding.root
    }

}
