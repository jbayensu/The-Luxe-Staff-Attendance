package com.acetechsol.auththree

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.acetechsol.auththree.adapters.StaffAttendanceAdapter
import com.acetechsol.auththree.database.Constants
import com.acetechsol.auththree.database.DbHelper
import java.util.*


class ManageStaffAttendanceFragment : Fragment() {

    private lateinit var staffAttendanceRV: RecyclerView
    private lateinit var item: MenuItem

    //db helper
    private lateinit var dbHelper: DbHelper

    //order by / sort queries
    private val newestFirst = "${Constants.STAFF_ADDED_TIMESTAMP} DESC"


    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)

        dbHelper = DbHelper(context)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_manage_staff_attendance, container, false)

        staffAttendanceRV = view.findViewById(R.id.staff_attendance_RV)

        loadRecord()
        return view
    }

    private fun loadRecord() {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = System.currentTimeMillis()
        //val currentDate = ""+ android.text.format.DateFormat.format("dd/MM/yyyy", calendar)

        val adapterRecord = StaffAttendanceAdapter(context, dbHelper.getAllStaffDetails())
        staffAttendanceRV.adapter = adapterRecord
    }

    private fun searchRecord(query: String) {
        val adapterRecord = StaffAttendanceAdapter(context, dbHelper.searchStaffRecords(query, newestFirst))
        staffAttendanceRV.adapter = adapterRecord
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = "Staff Attendance"
        loadRecord()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.search_menu, menu)
        item = menu.findItem(R.id.action_search)
        //item.isVisible = true
        val searchView = item.actionView as androidx.appcompat.widget.SearchView

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                //search as you type
                if (newText != null) {
                    searchRecord(newText)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                //search when button on keyboard is clicked
                if (query != null) {
                    searchRecord(query)
                }
                return true
            }

        })
    }



}