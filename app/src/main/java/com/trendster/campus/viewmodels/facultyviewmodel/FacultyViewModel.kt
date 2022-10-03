package com.trendster.campus.viewmodels.facultyviewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.trendster.campus.utils.* // ktlint-disable no-wildcard-imports
import java.text.SimpleDateFormat
import java.util.* // ktlint-disable no-wildcard-imports
import kotlin.collections.HashMap

class FacultyViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val storageReference = FirebaseStorage.getInstance().reference

    var subName = ""
    var branchName = ""
    var semName = ""

    private val _readFacultySubjects = MutableLiveData<List<DocumentSnapshot?>>()
    val readFacultySubjects: LiveData<List<DocumentSnapshot?>>
        get() = _readFacultySubjects

    private val _listStudents = MutableLiveData<List<DocumentSnapshot?>>()
    val listStudents: LiveData<List<DocumentSnapshot?>>
        get() = _listStudents

    private val _readAttendance = MutableLiveData<DocumentSnapshot?>()
    val readAttendance: LiveData<DocumentSnapshot?>
        get() = _readAttendance

    private val _attendanceStatus = MutableLiveData<Boolean>()
    val attendanceStatus: LiveData<Boolean>
        get() = _attendanceStatus

    private var _readClasswork = MutableLiveData<List<DocumentSnapshot?>>()
    val readClasswork: LiveData<List<DocumentSnapshot?>>
        get() = _readClasswork

    /** function to fetch faculty subjects */
    fun fetchFacultySubjects(facultyUID: String) {

        firestore.collection("Faculty").document(facultyUID)
            .collection("subjectList").get()
            .addOnSuccessListener { myData ->
                val docs = myData.documents
                _readFacultySubjects.postValue(docs)
            }
    }

    /** function to fetch students details from Firestore for faculty */
    fun fetchStudents(branch: String, semester: String) {
        firestore.collection("Users")
            .whereEqualTo(USER_BRANCH, branch)
            .whereEqualTo(USER_SEMESTER, semester)
            .addSnapshotListener { value, error ->
                val docs = value?.documents
                _listStudents.postValue(docs!!)
                Log.d("MYHI", docs.size.toString())
            }
    }

    /** Function to fetch attendance of a student from Firestore for faculty */
    fun fetchAttendance(studentUID: String, subjectName: String) {
        firestore.collection("Users")
            .document(studentUID).collection("Attendance")
            .document(subjectName)
            .addSnapshotListener { value, error ->

                _readAttendance.postValue(value)
            }
    }

    /** Function to update attendance to Firestore */
    fun updateAttendance(studentUID: String, subjectName: String, attendance: Map<String, Long>) {
        firestore.collection("Users")
            .document(studentUID).collection("Attendance")
            .document(subjectName).update(attendance).addOnSuccessListener {
                Log.d("DATAUPLd", "DONE")
                _attendanceStatus.postValue(true)
            }
            .addOnFailureListener {
                Log.d("DATAUPLd", "Error")
                _attendanceStatus.postValue(false)
            }
    }

    /** load data from Firestore to manage classwork fragment */
    fun loadClasswork() {

        firestore.collection("Data").document(branchName)
            .collection(semName).document("Subjects")
            .collection("list").document(subName)
            .collection("classwork")
            .addSnapshotListener { value, error ->

                val docs = value?.documents
                _readClasswork.postValue(docs!!)
                Log.d("loadData", docs?.size.toString())
            }
    }

    fun createClasswork( context: Context, workTitle: String, workDesc: String)
    {
        uploadData(context, workTitle, workDesc)
    }

    /** upload study materials link to Firestore */
    private fun uploadData( context: Context, workTitle: String, workDesc: String)
    {
        val data = HashMap<String, String>()
        data[ASSIGNMENT_TITLE] = workTitle
        data[ASSIGNMENT_DESC] = workDesc
        data[ASSIGNMENT_POST_DATE] = postDate()

        val part = firestore.collection("Data").document(branchName)
            .collection(semName).document("Subjects")
            .collection("list").document(subName)
            .collection("classwork").document(workTitle)

        part.set(data)
            .addOnSuccessListener {
                showToast(context, "Successful")
            }
            .addOnFailureListener {
                showToast(context, it.message)
                Log.d("upload failed", it.message!!)
            }
    }

    /** get current date and time on uploading study materials to Firestore */
    private fun postDate(): String {
        val today = Calendar.getInstance()
        val date = today.time
        Log.d("myTime", date.toString())
        Log.d("myTime", today.toString())
        val day = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)

        /**individual item */
        val year = today.get(Calendar.YEAR)
        val month = today.get(Calendar.MONTH)
        val myday = today.get(Calendar.DATE)

        val hour = today.get(Calendar.HOUR_OF_DAY)
        val minute = today.get(Calendar.MINUTE)
        Log.d("myTime11", myday.toString())
        /**individual item */

        return "$myday-$month-$year"
    }

    private fun showToast(context: Context, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
