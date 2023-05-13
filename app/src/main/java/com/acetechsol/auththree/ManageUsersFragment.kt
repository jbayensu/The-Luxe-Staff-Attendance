package com.acetechsol.auththree

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.acetechsol.auththree.adapters.UserRecordAdapter
import com.acetechsol.auththree.database.DbHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ManageUsersFragment : Fragment() {

    private lateinit var addUserRecord: FloatingActionButton
    private lateinit var userRecordsRV: RecyclerView
    //db helper
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DbHelper(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_manage_users, container, false)

        addUserRecord = view.findViewById(R.id.add_user_record_btn)
        userRecordsRV = view.findViewById(R.id.user_records_RV)

        loadRecord()

        addUserRecord.setOnClickListener{
            val navRegister = activity as FragmentNavigation
            val fragment = RegisterFragment()
            val bundle = Bundle()
            bundle.putString("USER_ID", "")
            fragment.arguments = bundle
            navRegister.navigateFrag(fragment, true)
            //navRegister.navigateFrag(AddUpdateStaffRecordFragment(), false)
        }

        return view
    }

    private fun loadRecord() {
        val adapterRecord = UserRecordAdapter(context, dbHelper.getUserRecords())
        userRecordsRV.adapter = adapterRecord
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = "Manage Users"
        loadRecord()
    }


}