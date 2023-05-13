package com.acetechsol.auththree

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.acetechsol.auththree.adapters.StaffRecordAdapter
import com.acetechsol.auththree.database.Constants
import com.acetechsol.auththree.database.DbHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ManageStaffRecordsFragment : Fragment() {

    private lateinit var addStaffRecord: FloatingActionButton
    private lateinit var staffRecordsRV: RecyclerView
    private lateinit var item: MenuItem

    //db helper
    private lateinit var dbHelper: DbHelper

    //order by / sort queries
    private val newestFirst = "${Constants.STAFF_ADDED_TIMESTAMP} DESC"

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        //activity or fragment
        //initialize db helper class
        dbHelper = DbHelper(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_manage_staff_records, container, false)

        addStaffRecord = view.findViewById(R.id.add_staff_record_btn)
        staffRecordsRV = view.findViewById(R.id.staff_records_RV)


        loadRecord()

        addStaffRecord.setOnClickListener{
            val navRegister = activity as FragmentNavigation
            val fragment = AddUpdateStaffRecordFragment()
            val bundle = Bundle()
            bundle.putInt("IS_EDIT_MODE", 0)
            fragment.arguments = bundle
            navRegister.navigateFrag(fragment, true)
            //navRegister.navigateFrag(AddUpdateStaffRecordFragment(), false)
        }

        return view
    }

    private fun loadRecord() {
        val adapterRecord = StaffRecordAdapter(context, dbHelper.getAllStaffDetails(newestFirst))
        staffRecordsRV.adapter = adapterRecord
    }

    private fun searchRecord(query: String) {
        val adapterRecord = StaffRecordAdapter(context, dbHelper.searchStaffRecords(query, newestFirst))
        staffRecordsRV.adapter = adapterRecord
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = "Manage Staff"
        loadRecord()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
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