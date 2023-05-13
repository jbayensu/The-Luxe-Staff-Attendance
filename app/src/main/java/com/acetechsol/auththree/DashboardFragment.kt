package com.acetechsol.auththree

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.acetechsol.auththree.database.DbHelper
import java.util.*


class DashboardFragment : Fragment() {
    private lateinit var currentTime: TextView
    private lateinit var totalNumberOfStaff: TextView
    private lateinit var totalPresentTV: TextView
    private lateinit var totalAbsentTV: TextView
    private lateinit var averageAttendanceTV: TextView
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(false)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_dashboard, container, false)

        currentTime = view.findViewById(R.id.current_date_view)
        totalNumberOfStaff = view.findViewById(R.id.total_number_of_staff_TV)
        totalPresentTV = view.findViewById(R.id.total_present_TV)
        totalAbsentTV = view.findViewById(R.id.total_absent)
        averageAttendanceTV = view.findViewById(R.id.average_attendance_TV)


        dbHelper = DbHelper(context)

        //Current date and Shift
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = System.currentTimeMillis()
        val currentDate = ""+ android.text.format.DateFormat.format("dd-MMMM-yyyy", calendar)
        val currentDate2 = ""+ android.text.format.DateFormat.format("dd-MM-yyyy", calendar)
        val timeOfDay  = calendar.get(Calendar.HOUR_OF_DAY)
        val session = if(timeOfDay in 0..15) "MORNING" else "EVENING"
        val displayDateShift = "$currentDate \n$session shift"
        currentTime.text = displayDateShift


        val totalPresent = dbHelper.countDailyAttendancePresent(currentDate2, session)
        val totalStaff = dbHelper.countStaffRecords()
        val totalAbsent = "${totalStaff - totalPresent}"

        val averageAttendance = if(totalStaff > 0) {
            (totalPresent / totalStaff.toDouble()) * 100

        }else{
            0.0
        }
        totalPresentTV.text = totalPresent.toString()
        totalNumberOfStaff.text = totalStaff.toString()
        totalAbsentTV.text = totalAbsent
        "${averageAttendance.toInt()}%".also { averageAttendanceTV.text = it }


        return view
    }
/*
    private fun getDate():String{
        val simpleDateFormat= SimpleDateFormat("dd-MMMM-yyyy", Locale.UK)
        return simpleDateFormat.format(Date())
    }

 */

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = "Dashboard"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.action_search).isVisible = false
    }


}