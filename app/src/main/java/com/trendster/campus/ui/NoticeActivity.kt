package com.trendster.campus.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.trendster.campus.databinding.ActivityNoticeBinding

class NoticeActivity : AppCompatActivity() {

    private var _binding: ActivityNoticeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.noticeWebView.loadUrl("http://makautexam.net/announcement/")


        showSnackBar()

        /** finishing activity on pressing back button present in action bar of notice activity */
        binding.closeButton.setOnClickListener {
            finish()
        }
    }

    /** function to show loading until notice loads */
    private fun showSnackBar() {
        val snackBar = Snackbar.make(binding.noticeLayout, "Loading Notices...", Snackbar.LENGTH_LONG)
        snackBar.show()
    }

    /** finishing activity on pressing back button */
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
