package com.trendster.campus.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.trendster.campus.R
import com.trendster.campus.databinding.ActivitySplashBinding
import com.trendster.campus.ui.faculty.FacultyActivity
import com.trendster.campus.viewmodels.userviewmodel.UserViewModel

class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding
    private lateinit var userViewModel: UserViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        auth = FirebaseAuth.getInstance()
        supportActionBar?.hide()

        /** If user is repeating user check either faculty or student , else open user activity */
        if (auth.currentUser != null) {
            userViewModel.readUserLevel(auth.currentUser!!.uid)
            userViewModel.readUserType.observe(
                this,
                {

                    /** If user is faculty, open faculty activity else open user activity */
                    if (it == true)
                        handleResult(Intent(this, FacultyActivity::class.java))
                    else
                        handleResult(Intent(this, UserActivity::class.java))
                }
            )
        } else
            handleResult(Intent(this, UserActivity::class.java))
    }

    /** function to handle repeated user check and open respective activity according to the condition satisfied */
    private fun handleResult(myIntent: Intent) {
        Handler().postDelayed(
            {
                startActivity(myIntent)
                finish()
            },
            1500
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
