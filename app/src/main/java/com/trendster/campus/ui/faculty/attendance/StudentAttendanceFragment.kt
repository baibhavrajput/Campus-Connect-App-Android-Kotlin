package com.trendster.campus.ui.faculty.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.trendster.campus.databinding.FragmentStudentAttendanceBinding
import com.trendster.campus.utils.CLASS_PRESENT
import com.trendster.campus.utils.CLASS_TOTAL
import com.trendster.campus.viewmodels.facultyviewmodel.FacultyViewModel

class StudentAttendanceFragment : DialogFragment() {

    private var _binding: FragmentStudentAttendanceBinding? = null
    private val binding get() = _binding!!
    val args: StudentAttendanceFragmentArgs by navArgs()
    private val facultyViewModel: FacultyViewModel by activityViewModels()

    private var presentClass: Long? = 0
    private var totalClass: Long? = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStudentAttendanceBinding.inflate(inflater, container, false)

        val studentInfo = args.studentInfo

        /** Fetch Subject title and Student Name in attendance fragment
         * carrying over Subject title and Student name from previous fragment */
        if (studentInfo.subjectName != null && studentInfo.studentUID != null) {
            binding.txtSubjectTitle.text = studentInfo.subjectName
            binding.txtStudentNme.text = studentInfo.studentName
            Toast.makeText(requireContext(), studentInfo.studentUID, Toast.LENGTH_SHORT).show()
            facultyViewModel.fetchAttendance(studentInfo.studentUID, studentInfo.subjectName)
        }

        /** Observe readAttendance from facultyViewModel and display present attendance and total attendance */
        facultyViewModel.readAttendance.observe(
            viewLifecycleOwner,
            { myData ->
                presentClass = myData?.get(CLASS_PRESENT) as Long?
                totalClass = myData?.get(CLASS_TOTAL) as Long?

                binding.txtPresentClass.text = presentClass.toString()
                binding.txtTotalClass.text = totalClass.toString()
            }
        )

        /** Clicking Present decrease button, decrease Present by 1 on each press */
        binding.btnPdecrease.setOnClickListener {
            presentClass = presentClass?.minus(1)
            binding.txtPresentClass.text = presentClass.toString()
        }

        /** Clicking Present increase button, increase Present by 1 on each press */
        binding.btnPincrease.setOnClickListener {
            presentClass = presentClass?.plus(1)
            binding.txtPresentClass.text = presentClass.toString()
        }

        /** Clicking Total decrease button, decrease Total by 1 on each press */
        binding.btnTdecrease.setOnClickListener {
            totalClass = totalClass?.minus(1)
            binding.txtTotalClass.text = totalClass.toString()
        }

        /** Clicking Total decrease button, decrease Total by 1 on each press */
        binding.btnTincrease.setOnClickListener {
            totalClass = presentClass?.plus(1)
            binding.txtTotalClass.text = totalClass.toString()
        }

        /** Observe attendanceStatus and show message if attendance is updated successfully else show error message */
        facultyViewModel.attendanceStatus.observe(
            viewLifecycleOwner,
            {
                if (it == true) {
                    showToast("Update Successful")
                } else {
                    showToast("Some Error Occurred")
                }
            }
        )

        /** Clicking Update Attendance button, update attendance to facultyViewModel which uploads attendance to firestore */
        binding.btnUpdateAttendance.setOnClickListener {

            val attendance = HashMap<String, Long>()
            attendance[CLASS_PRESENT] = presentClass!!
            attendance[CLASS_TOTAL] = totalClass!!

            if (studentInfo.subjectName != null && studentInfo.studentUID != null)
                facultyViewModel.updateAttendance(studentInfo.studentUID, studentInfo.subjectName, attendance)
        }

        return binding.root
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
