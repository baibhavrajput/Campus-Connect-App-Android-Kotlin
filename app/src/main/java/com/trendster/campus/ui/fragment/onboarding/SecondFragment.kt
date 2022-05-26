package com.trendster.campus.ui.fragment.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.trendster.campus.R
import com.trendster.campus.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.onboardingViewPager)

        /** On clicking next button, take to next introduction fragment */
        binding.btnNextPage.setOnClickListener {
            viewPager?.currentItem = 2
        }

        /** On clicking previous button, take to previous introduction fragment */
        binding.btnNextPrevious.setOnClickListener {
            viewPager?.currentItem = 0
        }

        return binding.root
    }
}
