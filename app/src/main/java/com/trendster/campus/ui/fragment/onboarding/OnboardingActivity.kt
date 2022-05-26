package com.trendster.campus.ui.fragment.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.trendster.campus.R
import com.trendster.campus.adapters.ViewPagerAdapter
import com.trendster.campus.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private var _binding: ActivityOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        /** List of Introduction Fragments */
        val fragmentList = arrayListOf<Fragment>(
            FirstFragment(),
            SecondFragment(),
            ThirdFragment()
        )

        /** Initializing view pager adapter */
        val mAdapter = ViewPagerAdapter(
            fragmentList,
            supportFragmentManager,
            lifecycle
        )

        /** binding ViewPager with onBoarding ViewPager */
        binding.onboardingViewPager.adapter = mAdapter
    }
}