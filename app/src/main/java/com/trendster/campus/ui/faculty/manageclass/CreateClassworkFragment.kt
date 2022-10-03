package com.trendster.campus.ui.faculty.manageclass

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.trendster.campus.databinding.FragmentCreateClassworkBinding
import com.trendster.campus.viewmodels.facultyviewmodel.FacultyViewModel
import java.io.File
import java.util.*

class CreateClassworkFragment : Fragment() {

    private var _binding: FragmentCreateClassworkBinding? = null
    private val binding get() = _binding!!
    private val REQ = 1
    private val facultyViewModel: FacultyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreateClassworkBinding.inflate(inflater, container, false)

        binding.btnCreateClasswork.setOnClickListener {
            val workTitle = binding.textFieldTitle
            val workDesc = binding.textFieldDesc

            when {
                workTitle.editText!!.text.isEmpty() -> {
                    workTitle.error = "Enter Title"
                }
                workDesc.editText!!.text.isEmpty() -> {
                    workDesc.error = "Enter Link"
                }
                else -> {

                    facultyViewModel.createClasswork(
                        requireContext(),
                        workTitle.editText!!.text.toString(),
                        workDesc.editText!!.text.toString(),
                    )
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
